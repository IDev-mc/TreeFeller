package de.I_Dev.TF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeFeller extends JavaPlugin implements Listener {

	public List<String> treematerials = new ArrayList<>();
	public int maxTreeSize = 0;
	public int maxTreeHeight = 0;
	public boolean toolDamage = false;
	public boolean onsneak = false;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		super.onEnable();
		Bukkit.getPluginManager().registerEvents(this, this);

		getConfig().options().copyDefaults(true);
		saveConfig();
		maxTreeSize = getConfig().getInt("maxTreeSize");
		maxTreeHeight = getConfig().getInt("maxTreeHeight");
		toolDamage = getConfig().getBoolean("toolDamage");
		onsneak = getConfig().getBoolean("activateonsneak");
		treematerials.addAll((Collection<? extends String>) getConfig().getList("treeblocks"));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockMine(BlockBreakEvent e) {
		if (e.isCancelled()) return;
		if(onsneak && !e.getPlayer().isSneaking()) return;
		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		if (!isMaterial(e.getBlock().getType())) return;
		if (!isTree(e.getBlock().getLocation())) return;

		breakTree(e.getBlock()); 
	}

	private void breakTree(Block block) {
		List<Block> blocks = new ArrayList<>();
		blocks.add(block);

		for (int i = 0; i < blocks.size(); i++) {
			getBlocksAroundALocation(blocks.get(i).getLocation(), blocks);
		}

		if (blocks.size() >= maxTreeSize) return;

		for (Block b : blocks) {
			if(toolDamage && b == block) continue;
			
			b.breakNaturally();
		}
	}

	private List<Block> getBlocksAroundALocation(Location loc, List<Block> nonLoop) {
		List<Block> blocks = nonLoop != null ? nonLoop : new ArrayList<>();
		for (int x = -1; x <= 1; x = x + 1) {
			for (int y = -1; y <= 1; y = y + 1) {
				for (int z = -1; z <= 1; z = z + 1) {
					Block block = loc.clone().add(x, y, z).getBlock();
					if (isMaterial(block.getType()) && !blocks.contains(block)) {
						blocks.add(block);
					}
				}
			}
		}

		return blocks;
	}

	private boolean isMaterial(Material material) {
		for(String materials : treematerials) {
			if(material.toString().toLowerCase().contains(materials.toLowerCase())) return true;
		}
		return false;
	}

	private boolean isTree(Location loc) {
		if (loc == null) return false;
		
		int height = 1;
		boolean isTree = false;
		Material material = loc.getBlock().getType();
		
		while (loc.add(0, 1, 0).getBlock().getType() == material) {
			height++;
			
			if(height >= maxTreeHeight) return false;
			if(checkForLeaves(loc)) isTree = true;
		}
		return isTree;
	}
	
	private boolean checkForLeaves(Location loc) {
		for (int y = 0; y <= 4; y = y + 1) {
			for (int x = -1; x <= 1; x = x + 1) {
				for (int z = -1; z <= 1; z = z + 1) {
					Block block = loc.clone().add(x, y, z).getBlock();
					if (block.getType().toString().contains("LEAVES") ||
							block.getType().toString().contains("WART_BLOCK")) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
