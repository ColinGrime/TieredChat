package com.github.scilldev.data;

import com.github.scilldev.TieredChat;
import com.github.scilldev.utils.Logger;
import com.github.scilldev.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Messages {

	INVALID_PERMISSION("invalid-permission", "&cYou do not have permission to perform this command."),
	INVALID_SENDER("invalid-sender", "&cYou must be a player to perform this command."),
	RELOADED("reloaded", "&aTieredChat has been reloaded!"),
	CHAT_USAGE("chat-usage", "&c/chat [channel]"),
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
		if (config.getString(path) != null) {
			messages = Collections.singletonList(Utils.color(config.getString(path)));
		} else if (!config.getStringList(path).isEmpty()) {
			messages = Utils.color(config.getStringList(path));
		} else {
			Logger.log("Config value \"" + path + "\" has failed to load. Using default value...");
			messages = Utils.color(messages);
		}
	}

	public void sendTo(CommandSender sender) {
		if (messages.isEmpty())
			return;

		messages.forEach(sender::sendMessage);
	}
}
