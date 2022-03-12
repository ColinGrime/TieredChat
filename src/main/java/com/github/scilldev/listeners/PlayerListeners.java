package com.github.scilldev.listeners;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.channel.ChatChannel;
import com.github.scilldev.events.ChannelChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;

public class PlayerListeners implements Listener {

	private final TieredChat plugin;

	public PlayerListeners(TieredChat plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();
		ChatChannel channel = plugin.getChatManager().getUser(player).getChannelPreference();

		Bukkit.getScheduler().runTask(plugin, () -> {
			Set<Player> recipients = channel.getAllowedListeners(player);
			ChannelChatEvent channelEvent = new ChannelChatEvent(channel, player, event.getMessage(), recipients);
			Bukkit.getServer().getPluginManager().callEvent(channelEvent);
		});

		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getUserData().saveUser(event.getPlayer().getUniqueId()));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getUserData().loadUser(event.getPlayer().getUniqueId()));
	}
}
