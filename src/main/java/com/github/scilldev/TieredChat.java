package com.github.scilldev;

import com.github.scilldev.chat.manager.ChatManager;
import com.github.scilldev.chat.manager.ChatManagerAbstraction;
import com.github.scilldev.commands.chat.ChatBaseCommand;
import com.github.scilldev.commands.chat.subcommands.ChatSwapSubCommand;
import com.github.scilldev.commands.filter.FilterBaseCommand;
import com.github.scilldev.commands.filter.subcommands.FilterAddSubCommand;
import com.github.scilldev.commands.filter.subcommands.FilterListSubCommand;
import com.github.scilldev.commands.filter.subcommands.FilterRemoveSubCommand;
import com.github.scilldev.data.DataSourceProvider;
import com.github.scilldev.data.mysql.Database;
import com.github.scilldev.data.mysql.user.UserData;
import com.github.scilldev.data.yaml.ChannelData;
import com.github.scilldev.data.yaml.Messages;
import com.github.scilldev.data.yaml.Settings;
import com.github.scilldev.listeners.ChannelListener;
import com.github.scilldev.listeners.PlayerListeners;
import com.github.scilldev.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TieredChat extends JavaPlugin {

	// manager classes
	private ChatManager chatManager;

	// yaml data classes
	private Settings settings;
	private ChannelData channelData;

	// sql data classes
	private DataSourceProvider dataSourceProvider;
	private UserData userData;

	@Override
	public void onEnable() {
		if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			disablePlugin("PlaceholderAPI was not found. Plugin has been disabled.");
			return;
		}

		// load managers and yaml data
		chatManager = new ChatManagerAbstraction();
		loadData();

		// initialize data provider and test connection
		dataSourceProvider = new DataSourceProvider(settings);
		if (!dataSourceProvider.testConection()) {
			disablePlugin("Could not establish database connection. Plugin has been disabled.");
			return;
		}

		// load more sql stuff
		Database database = new Database(dataSourceProvider.getSource());
		database.init();
		database.startTimers();

		// register commands/subcommands
		ChatBaseCommand chatCommand = new ChatBaseCommand(this);
		chatCommand.registerSubCommand(new ChatSwapSubCommand(this));

		FilterBaseCommand filterCommand = new FilterBaseCommand(this);
		filterCommand.registerSubCommand(new FilterAddSubCommand(this));
		filterCommand.registerSubCommand(new FilterRemoveSubCommand(this));
		filterCommand.registerSubCommand(new FilterListSubCommand(this));

		// register listeners
		new PlayerListeners(this);
		new ChannelListener(this);
	}

	@Override
	public void onDisable() {
		dataSourceProvider.close();
		// TODO save files / SQL
	}

	private void loadData() {
		saveDefaultConfig();
		settings = new Settings(this);
		channelData = new ChannelData(this);
		reload();
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

	public ChatManager getChatManager() {
		return chatManager;
	}

	private void disablePlugin(String message) {
		Logger.severe(message);
		getServer().getPluginManager().disablePlugin(this);
	}
}
