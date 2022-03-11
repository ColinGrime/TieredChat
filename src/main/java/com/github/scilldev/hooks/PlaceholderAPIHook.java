package com.github.scilldev.hooks;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.ChatUser;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	private final TieredChat plugin;

	public PlaceholderAPIHook(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getIdentifier() {
		return "tieredchat";
	}

	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().get(0);
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		ChatUser user = plugin.getChatManager().getUser(player);

		// %tieredchat_channel%
		if (identifier.equalsIgnoreCase("channel")) {
			return user.getChannelPreference().getName();
		}

		return null;
	}
}
