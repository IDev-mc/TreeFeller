package de.I_Dev.TF;

import java.util.ArrayList;
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

public class Main extends JavaPlugin implements Listener {

	public List<String> treematerials = new ArrayList<>();
	public int maxTreeSize = 0;
	public int maxTreeHeight = 0;

	@Override
	public void onEnable() {
		super.onEnable();
		Bukkit.getPluginManager().registerEvents(this, this);

		treematerials.add("LOG");
		treematerials.add("WOOD");
		treematerials.add("STEM");
		treematerials.add("HYPHAE");

		getConfig().options().copyDefaults(true);
		saveConfig();
		maxTreeSize = getConfig().getInt("maxTreeSize");
		maxTreeHeight = getConfig().getInt("maxTreeHeight");
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void blockMine(BlockBreakEvent e) {
		if (e.isCancelled()) return;
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
			if(material.toString().contains(materials)) return true;
		}
		return false;
	}

	private boolean isTree(Location loc) {
		if (loc == null)
			return false;
		int height = 1;
		boolean isTree = false;
		
		while (loc.add(0, 1, 0).getBlock().getType() != Material.AIR) {
			height++;
			if(height >= maxTreeHeight) {
				return false;
			}
			
			
			for (int y = 0; y <= 4; y = y + 1) {
				for (int x = -1; x <= 1; x = x + 1) {
					for (int z = -1; z <= 1; z = z + 1) {
						if (loc.clone().add(x, y, z).getBlock().getType().toString().contains("LEAVES")) {
							isTree = true;
						}
					}
				}
			}
		}
		return isTree;
	}
}
