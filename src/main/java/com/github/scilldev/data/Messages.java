package com.github.scilldev.data;

import com.github.scilldev.TieredChat;
import com.github.scilldev.utils.Logger;
import com.github.scilldev.utils.Replacer;
import com.github.scilldev.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public enum Messages {

	// success messages
	SUCCESS_CHAT_SWAP("success.chat-swap", "&7You have switched your chat to &a%channel%&7!"),
	SUCCESS_FILTER_ADD("success.filter-add", "&a%word% &7has been added to the filter!"),
	SUCCESS_FILTER_REMOVE("success.filter-remove", "&a%word% &7 has been removed from the filter!"),
	SUCCESS_FILTER_LIST("success.filter-list", "&7Words: &a%words%"),

	// usage messages
	USAGE_CHAT_COMMAND("usage.chat-command", "&c/chat [channel]"),
	USAGE_FILTER_COMMAND("usage.filter-command", "&c/filter"),
	USAGE_FILTER_ADD("usage.filter-add", "&c/filter add [word]"),
	USAGE_FILTER_REMOVE("usage.filter-remove", "&c/filter remove [word]"),
	USAGE_FILTER_LIST("usage.filter-list", "&c/filter list"),

	// invalid messages
	INVALID_PERMISSION("invalid.permission", "&cYou do not have permission to perform this command."),
	INVALID_SENDER("invalid.sender", "&cYou must be a player to perform this command."),

	// admin messages
	RELOADED("admin.reloaded", "&aTieredChat has been reloaded!"),
	;

	private static FileConfiguration config;
	private final String path;
	private List<String> messages;

	Messages(String path, String...messages) {
		this.path = "messages." + path;
		this.messages = Arrays.asList(messages);
	}

	public static void init(TieredChat plugin) {
		config = plugin.getConfig();
		Arrays.stream(Messages.values()).forEach(Messages::update);
	}

	private void update() {
		if (!config.getStringList(path).isEmpty()) {
			messages = Utils.color(config.getStringList(path));
		} else {
			Logger.log("Config path \"" + path + "\" has failed to load (using default value).");
			messages = Utils.color(messages);
		}
	}

	public void sendTo(CommandSender sender) {
		if (messages.isEmpty()) {
			return;
		}

		messages.forEach(sender::sendMessage);
	}

	public void sendTo(CommandSender sender, Replacer replacer) {
		if (messages.isEmpty()) {
			return;
		}

		replacer.replace(messages).forEach(sender::sendMessage);
	}
}
