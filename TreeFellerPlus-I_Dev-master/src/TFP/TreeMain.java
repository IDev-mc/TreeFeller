package TFP;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeMain extends JavaPlugin implements Listener{
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	/*
	 * Triggering block-cutter
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void a(BlockBreakEvent e) {
		if(e.isCancelled()) return;
		if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		String type = e.getBlock().getType().toString();
		if(!type.contains("LOG")
				&& !type.contains("WOOD")
				&& !type.contains("STEM")
				&& !type.contains("HYPHAE")) return;
		
		cutter(e.getBlock().getLocation(), e.getPlayer());
	}

	
	/*
	 * Cutting a block every tick (recursively)
	 */
	private void cutter(Location blocklocation, Player player) {
		for(Location woodlocation : checker(blocklocation)) {
			
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				@Override
				public void run() {
					woodlocation.getBlock().breakNaturally();
					cutter(woodlocation, player);
				}
			}, 1L);
		}
	}

	
	/*
	 * Checking in a 3 block diameter if there is a block with the name "LOG" in it
	 */
	private ArrayList<Location> checker(Location loc) {
		ArrayList<Location> locs = new ArrayList<>();
		for(int x = -1; x <= 1; x = x + 1) {
			for(int y = 0; y <= 1; y = y + 1) {
				for(int z = -1; z <= 1; z = z + 1) {
					String type = loc.clone().add(x, y, z).getBlock().getType().toString();
					if(type.contains("LOG")
							|| type.contains("WOOD")
							|| type.contains("STEM")
							|| type.contains("HYPHAE")) {
						locs.add(loc.clone().add(x, y, z));
					}
				}
			}
		}
		return locs;
	}

}
