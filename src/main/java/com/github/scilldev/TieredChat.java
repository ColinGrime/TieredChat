package com.github.scilldev;

import com.github.scilldev.chat.manager.ChatManager;
import com.github.scilldev.chat.manager.ChatManagerAbstraction;
import com.github.scilldev.commands.chat.ChatBaseCommand;
import com.github.scilldev.commands.chat.subcommands.ChatReloadSubCommand;
import com.github.scilldev.commands.chat.subcommands.ChatSwapSubCommand;
import com.github.scilldev.commands.filter.FilterBaseCommand;
import com.github.scilldev.commands.filter.subcommands.FilterAddSubCommand;
import com.github.scilldev.commands.filter.subcommands.FilterListSubCommand;
import com.github.scilldev.commands.filter.subcommands.FilterRemoveSubCommand;
import com.github.scilldev.data.DataSourceProvider;
import com.github.scilldev.data.mysql.Database;
import com.github.scilldev.data.mysql.DatabaseAbstraction;
import com.github.scilldev.data.mysql.user.UserData;
import com.github.scilldev.data.mysql.user.UserDataAbstraction;
import com.github.scilldev.data.yaml.ChannelData;
import com.github.scilldev.data.yaml.Messages;
import com.github.scilldev.data.yaml.Settings;
import com.github.scilldev.hooks.PlaceholderAPIHook;
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
	private Database database;

	private boolean isPlaceholdersEnabled = false;

	@Override
	public void onEnable() {
		// load managers and yaml data
		chatManager = new ChatManagerAbstraction();
		loadData();

		// initialize data provider and test connection
		dataSourceProvider = new DataSourceProvider(settings);
		if (!dataSourceProvider.testConection()) {
			Logger.severe("Could not establish database connection. Plugin has been disabled.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// user data
		userData = new UserDataAbstraction(this);

		// set up the database (build needed tables / perform updates)
		Logger.log("Setting up database...");
		timeAction(() -> database = new DatabaseAbstraction(this), "Database set up in %s ms");

		// start database save timers (for user chat data)
		Logger.log("Loading in user data...");
		timeAction(() -> userData.loadUsers(), "Users loaded in %s ms");
		database.startTimers();

		// register commands and listeners
		registerCommands();
		registerListeners();

		// check for PlaceholderAPI
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new PlaceholderAPIHook(this).register();
			isPlaceholdersEnabled = true;
			Logger.log("Placeholders have been registered.");
		}
	}

	@Override
	public void onDisable() {
		if (userData != null && dataSourceProvider.getSource() != null) {
			userData.saveUsers();
			dataSourceProvider.close();
		}
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

	private void registerCommands() {
		ChatBaseCommand chatCommand = new ChatBaseCommand(this);
		chatCommand.registerSubCommand(new ChatSwapSubCommand(this));
		chatCommand.registerSubCommand(new ChatReloadSubCommand(this));

		FilterBaseCommand filterCommand = new FilterBaseCommand(this);
		filterCommand.registerSubCommand(new FilterAddSubCommand(this));
		filterCommand.registerSubCommand(new FilterRemoveSubCommand(this));
		filterCommand.registerSubCommand(new FilterListSubCommand(this));
	}

	private void registerListeners() {
		new PlayerListeners(this);
		new ChannelListener(this);
	}

	public Settings getSettings() {
		return settings;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}

	public DataSourceProvider getDataSourceProvider() {
		return dataSourceProvider;
	}

	public UserData getUserData() {
		return userData;
	}

	public boolean isPlaceholdersEnabled() {
		return isPlaceholdersEnabled;
	}

	public void timeAction(Action action, String complete) {
		long time = System.currentTimeMillis();
		action.run();
		Logger.log(String.format(complete, System.currentTimeMillis() - time));
	}

	@FunctionalInterface
	public interface Action {
		void run();
	}
}
