package com.github.scilldev.data;

import com.github.scilldev.TieredChat;
import com.github.scilldev.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings implements DataFile {

	private final TieredChat plugin;
	private FileConfiguration config;

	private String chatFormat;

	public Settings(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void reload() {
		config = plugin.getConfig();

		chatFormat = _getChatFormat();
	}

	private String _getChatFormat() {
		return Utils.color(config.getString("chat-format"));
	}

	public String getChatFormat() {
		return chatFormat;
	}
}
