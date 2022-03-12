package com.github.scilldev.chat.channel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatChannelAbstraction implements ChatChannel {

	private final String name;
	private final List<String> commands;
	private final String permission;
	private final DisplayType displayType;
	private final double blockRadius;

	public ChatChannelAbstraction(String name, List<String> commands, String permission, DisplayType displayType, double blockRadius) {
		this.name = name;
		this.commands = commands;
		this.permission = permission;
		this.displayType = displayType;
		this.blockRadius = blockRadius <= 0 ? -1 : blockRadius;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getCommands() {
		return commands;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public boolean hasPermission(Player player) {
		return permission.isEmpty() || player.hasPermission(permission);
	}

	@Override
	public DisplayType getDisplayType() {
		return displayType;
	}

	@Override
	public double getBlockRadius() {
		return blockRadius;
	}

	@Override
	public Set<Player> getCurrentListeners() {
		Set<Player> listeners = new HashSet<>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			// TODO add ability to mute channels
			if (player.hasPermission(permission)) {
				listeners.add(player);
			}
		}

		return listeners;
	}

	@Override
	public Set<Player> getAllowedListeners(Player player) {
		Set<Player> listeners = getCurrentListeners();
		listeners.remove(player);

		if (displayType == DisplayType.GLOBAL) {
			return listeners;
		}

		if (blockRadius == -1) {
			return listeners.stream().filter(l -> l.getWorld() == player.getWorld()).collect(Collectors.toSet());
		}

		List<Entity> nearby = player.getNearbyEntities(blockRadius, blockRadius, blockRadius);
		return listeners.stream().filter(nearby::contains).collect(Collectors.toSet());
	}
}
