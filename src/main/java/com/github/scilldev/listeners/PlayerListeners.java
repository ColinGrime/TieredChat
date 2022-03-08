package com.github.scilldev.listeners;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.channel.ChatChannel;
import com.github.scilldev.chat.ChatUser;
import com.github.scilldev.events.ChannelChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerListeners implements Listener {

	private final TieredChat plugin;

	public PlayerListeners(TieredChat plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		ChatUser user = plugin.getChatManager().getUser(player);
		ChatChannel channel = user.getChannelPreference();

		ChannelChatEvent channelEvent = new ChannelChatEvent(channel, user, event.getMessage(), channel.getAllowedListeners(user));
		Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(channelEvent));

		event.setCancelled(true);
	}

	@EventHandler
	public void onCommandEvent(PlayerCommandPreprocessEvent event) {
		// todo command stuff like /global
		String commandName = event.getMessage().split(" ")[0];
//		if (event.getMessage())
	}
}
