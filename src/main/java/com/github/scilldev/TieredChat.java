package com.github.scilldev;

import com.github.scilldev.chat.manager.ChatManager;
import com.github.scilldev.chat.manager.ChatManagerAbstraction;
import com.github.scilldev.commands.chat.ChatBaseCommand;
import com.github.scilldev.commands.chat.subcommands.ChatSwapSubCommand;
import com.github.scilldev.data.ChannelDataFile;
import com.github.scilldev.data.Messages;
import com.github.scilldev.data.Settings;
import com.github.scilldev.listeners.ChannelListener;
import com.github.scilldev.listeners.PlayerListeners;
import com.github.scilldev.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TieredChat extends JavaPlugin {

	// data classes
	private Settings settings;
	private ChannelDataFile channelData;

	private ChatManager chatManager;

	@Override
	public void onEnable() {
		if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			Logger.severe("PlaceholderAPI was not found. Plugin has been disabled.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// load managers
		chatManager = new ChatManagerAbstraction();

		// create and reload config files
		loadData();
		reload();

		// register commands/subcommands
		ChatBaseCommand chatCommand = new ChatBaseCommand(this);
		chatCommand.registerSubCommand(new ChatSwapSubCommand(this));

		// register listeners
		new PlayerListeners(this);
		new ChannelListener(this);
	}

	@Override
	public void onDisable() {
		// TODO save files / SQL
	}

	private void loadData() {
		saveDefaultConfig();
		settings = new Settings(this);
		channelData = new ChannelDataFile(this);
	}

	public void reload() {
		reloadConfig();
		settings.reload();
		channelData.reload();
		Messages.init(this);
	}

	public Settings getSettings() {
		return settings;
	}

	public ChannelDataFile getChannelData() {
		return channelData;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}
}
