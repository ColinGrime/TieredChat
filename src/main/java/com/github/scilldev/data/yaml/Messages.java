package com.github.scilldev.data.yaml;

import com.github.scilldev.TieredChat;
import com.github.scilldev.utils.Logger;
import com.github.scilldev.utils.Replacer;
import com.github.scilldev.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Messages {

	// success messages
	SUCCESS_CHAT_SWAP("success.chat-swap", "&7You have switched your chat to &a%channel%&7!"),
	SUCCESS_FILTER_ADD("success.filter-add", "&a%word% &7has been added to the filter!"),
	SUCCESS_FILTER_REMOVE("success.filter-remove", "&a%word% &7 has been removed from the filter!"),
	SUCCESS_FILTER_LIST("success.filter-list", "&7Words: &a%words%"),

	// failure messages
	FAILURE_ALREADY_ADDED("failure.already-added", "&c%word% is already in your filter."),

	// usage messages
	USAGE_CHAT_COMMAND("usage.chat-command", "&7Chat commands:", "&a/chat [channel] &7- switches the channel", "&a/filter &7- block messages from showing up"),
	USAGE_FILTER_COMMAND("usage.filter-command", "&7Filter commands:", "&a/filter add [word] &7- add a word to your filter", "&a/filter remove [word] &7- remove a word from your filter", "&a/filter list &7- display the words in your filter"),
	USAGE_FILTER_ADD("usage.filter-add", "&7Usage: &a/filter add [word]"),
	USAGE_FILTER_REMOVE("usage.filter-remove", "&7Usage: &a/filter remove [word]"),
	USAGE_FILTER_LIST("usage.filter-list", "&7Usage: &a/filter list"),

	// invalid messages
	INVALID_PERMISSION("invalid.permission", "&cYou do not have permission to perform this command."),
	INVALID_SENDER("invalid.sender", "&cYou must be a player to perform this command."),
	INVALID_WORD("invalid.word", "&c%word% is not in your filter."),

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
		} else if (config.getString(path) != null) {
			messages = Collections.singletonList(Utils.color(config.getString(path)));
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
