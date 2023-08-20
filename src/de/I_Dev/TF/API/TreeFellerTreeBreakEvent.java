package de.I_Dev.TF.API;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public  class TreeFellerTreeBreakEvent extends Event implements Cancellable{

	private boolean cancelled = false;
	
	private static final HandlerList HANDLERS = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	private Player player;
	private List<Block> blocks;
	private Block block;
	
	public TreeFellerTreeBreakEvent(Player player, List<Block> blocks, Block block) {
		this.player = player;
		this.blocks = blocks;
		this.block = block;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public List<Block> getBlocks() {
		return this.blocks;
	}
	
	public Block getBrokenBlock() {
		return this.block;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}
}
