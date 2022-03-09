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

		callChannelEvent(channel, player, event.getMessage());
		event.setCancelled(true);
	}

	@EventHandler
	public void onCommandEvent(PlayerCommandPreprocessEvent event) {
		String commandName = event.getMessage().substring(1).split(" ")[0];
		ChatChannel channel = plugin.getChatManager().getChannelByName(commandName);

		if (channel != null && event.getPlayer().hasPermission(channel.getPermission())) {
			String message = event.getMessage().substring(commandName.length() + 2);

			callChannelEvent(channel, event.getPlayer(), message);
			event.setCancelled(true);
		}
	}

	private void callChannelEvent(ChatChannel channel, Player player, String message) {
		Bukkit.getScheduler().runTask(plugin, () -> {
			Set<Player> recipients = channel.getAllowedListeners(player);
			ChannelChatEvent channelEvent = new ChannelChatEvent(channel, player, message, recipients);
			Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(channelEvent));
		});
	}
}
