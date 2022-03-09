package com.github.scilldev.chat.channel;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.ChatUser;
import com.github.scilldev.chat.channel.enums.DisplayType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ChatChannelAbstraction implements ChatChannel {

	private final TieredChat main;

	private final String name;
	private final List<String> commands;
	private final String permission;
	private final DisplayType displayType;
	private final double blockRadius;

	public ChatChannelAbstraction(TieredChat main, String name) {
		this(main, name, new ArrayList<>(), "", DisplayType.GLOBAL, -1);
	}

	public ChatChannelAbstraction(TieredChat main, String name, List<String> commands, String permission, DisplayType displayType, double blockRadius) {
		this.main = main;
		this.name = returnNonNull(name, "InvalidName");
		this.commands = returnNonNull(commands, new ArrayList<>());
		this.permission = returnNonNull(permission, "");
		this.displayType = returnNonNull(displayType, DisplayType.GLOBAL);
		this.blockRadius = blockRadius <= 0 ? -1 : blockRadius;
	}

	private <T> T returnNonNull(T valueToCheck, T nonNull) {
		return valueToCheck == null ? nonNull : valueToCheck;
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
			ChatUser user = main.getChatManager().getUser(player);
			if (user.getChannelPreference() == this) {
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
