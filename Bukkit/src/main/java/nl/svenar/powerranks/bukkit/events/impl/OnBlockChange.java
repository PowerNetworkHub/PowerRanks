package nl.svenar.powerranks.bukkit.events.impl;

import java.io.File;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.MoistureChangeEvent;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.addons.PowerRanksAddon;
import nl.svenar.powerranks.bukkit.addons.PowerRanksPlayer;
import nl.svenar.powerranks.bukkit.addons.PowerRanksAddon.BlockChangeCause;

public class OnBlockChange implements Listener {

	PowerRanks m;

	public OnBlockChange(PowerRanks m) {
		this.m = m;
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent e) {
		final Player player = e.getPlayer();
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(player, block, BlockChangeCause.BREAK));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent e) {
		final Player player = e.getPlayer();
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(player, block, BlockChangeCause.PLACE));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockMoistureChange(final MoistureChangeEvent e) {
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(null, block, BlockChangeCause.PLACE));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockFertilize(final BlockFertilizeEvent e) {
		final Player player = e.getPlayer();
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(player, block, BlockChangeCause.PLACE));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockGrow(final BlockGrowEvent e) {
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(null, block, BlockChangeCause.PLACE));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockIgnite(final BlockIgniteEvent e) {
		final Player player = e.getPlayer();
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(player, block, BlockChangeCause.PLACE));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockExplode(final BlockExplodeEvent e) {
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(null, block, BlockChangeCause.PLACE));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBurn(final BlockBurnEvent e) {
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(null, block, BlockChangeCause.BURN));
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockRedstoneChange(final BlockFertilizeEvent e) {
		final Player player = e.getPlayer();
		final Block block = e.getBlock();

		e.setCancelled(handleEvent(player, block, BlockChangeCause.PLACE));
	}

	public boolean handleEvent(Player player, Block block, BlockChangeCause blockChangeCause) {
		boolean cancelled = false;
		try {
			for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
				PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
				if (prAddon.getValue().onBlockChange(prPlayer, block, blockChangeCause))
					cancelled = true;
			}
		} catch (Exception ex) {
		}
		return cancelled;
	}
} 