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
		this.name = name;
		this.commands = commands;
		this.permission = permission;
		this.displayType = displayType;
		this.blockRadius = blockRadius;
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
	public Set<ChatUser> getCurrentListeners() {
		Set<ChatUser> listeners = new HashSet<>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			ChatUser user = main.getChatManager().getUser(player);
			if (user.getChannelPreference() == this) {
				listeners.add(user);
			}
		}

		return listeners;
	}

	@Override
	public Set<ChatUser> getAllowedListeners(ChatUser chatUser) {
		if (displayType == DisplayType.GLOBAL) {
			return new HashSet<>();
		}

		Player player = chatUser.getPlayer();
		if (blockRadius == -1) {
			return getCurrentListeners().stream().filter(u -> u.getPlayer().getWorld() == player.getWorld()).collect(Collectors.toSet());
		}

		List<Entity> nearby = player.getNearbyEntities(blockRadius, blockRadius, blockRadius);
		return getCurrentListeners().stream().filter(u -> nearby.contains(u.getPlayer())).collect(Collectors.toSet());
	}
}
