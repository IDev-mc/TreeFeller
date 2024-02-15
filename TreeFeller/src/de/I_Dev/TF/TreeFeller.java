package de.I_Dev.TF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.I_Dev.TF.API.TreeFellerTreeBreakEvent;

public class TreeFeller extends JavaPlugin implements Listener {

	public static List<String> treematerials = new ArrayList<>();
	public static int maxTreeSize = 0;
	public static int maxTreeHeight = 0;
	public static boolean toolDamage = false;
	public static boolean onsneak = false;

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

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockBreak(BlockBreakEvent e) {
		if (e.isCancelled())
			return;
		Player p = e.getPlayer();
		if (onsneak && !p.isSneaking())
			return;
		if (p.getGameMode() != GameMode.SURVIVAL)
			return;
		if (!isMaterial(e.getBlock().getType()))
			return;
		if (!isTree(e.getBlock().getLocation()))
			return;

		List<Block> blocks = getTree(e.getBlock());

		TreeFellerTreeBreakEvent event = new TreeFellerTreeBreakEvent(p, blocks, e.getBlock());
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled())
			blocks.forEach(Block::breakNaturally);

		/*
		 * DEPRECATED, here to make it a smaller file. Next version has to have multiple
		 * files, for lower and higher versions
		 */

		if (!toolDamage)
			return;

		PlayerItemDamageEvent damageEvent = new PlayerItemDamageEvent(p, p.getInventory().getItemInHand(), 1);
		Bukkit.getPluginManager().callEvent(damageEvent);
		if (damageEvent.isCancelled())
			return;
		p.getInventory().getItemInHand()
				.setDurability((short) (p.getInventory().getItemInHand().getDurability() + damageEvent.getDamage()));
	}

	private List<Block> getTree(Block block) {
		List<Block> blocks = new ArrayList<>();
		blocks.add(block);
		for (int i = 0; i < blocks.size(); i++) {
			getBlocksAroundALocation(blocks.get(i).getLocation(), blocks);
		}
		if (blocks.size() >= maxTreeSize)
			return new ArrayList<>();
		return blocks;
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
		for (String materials : treematerials) {
			if (material.toString().toLowerCase().contains(materials.toLowerCase()))
				return true;
		}
		return false;
	}

	private boolean isTree(Location loc) {
		if (loc == null)
			return false;

		int height = 1;
		boolean isTree = false;
		Material material = loc.getBlock().getType();
		while (loc.add(0, 1, 0).getBlock().getType() == material) {
			height++;
			if (height >= maxTreeHeight)
				return false;
			if (checkForLeaves(loc)) {
				isTree = true;
				break;
			}
		}
		return isTree;
	}

	private boolean checkForLeaves(Location loc) {
		for (int y = 0; y <= 4; y = y + 1) {
			for (int x = -1; x <= 1; x = x + 1) {
				for (int z = -1; z <= 1; z = z + 1) {
					Block block = loc.clone().add(x, y, z).getBlock();
					if (block.getType().toString().contains("LEAVES")
							|| block.getType().toString().contains("WART_BLOCK")) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
