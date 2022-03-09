package com.github.scilldev.listeners;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.channel.ChatChannel;
import com.github.scilldev.events.ChannelChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Set;

public class PlayerListeners implements Listener {

	private final TieredChat plugin;

	public PlayerListeners(TieredChat plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		ChatChannel channel = plugin.getChatManager().getUser(player).getChannelPreference();
		callChannelEvent(channel, player, event.getMessage(), channel.getAllowedListeners(player));

		event.setCancelled(true);
	}

	@EventHandler
	public void onCommandEvent(PlayerCommandPreprocessEvent event) {
		String commandName = event.getMessage().substring(1).split(" ")[0];
		ChatChannel channel = plugin.getChatManager().getChannelByName(commandName);

		if (channel != null) {
			String message = event.getMessage().substring(commandName.length() + 1);
			Player player = event.getPlayer();
			callChannelEvent(channel, player, message, channel.getAllowedListeners(player));
		}
	}

	private void callChannelEvent(ChatChannel channel, Player player, String message, Set<Player> recipients) {
		ChannelChatEvent channelEvent = new ChannelChatEvent(channel, player, message, recipients);
		Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(channelEvent));
	}
}
