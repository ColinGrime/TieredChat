package com.github.scilldev.data.yaml;

import com.github.scilldev.TieredChat;
import com.github.scilldev.data.DataFile;
import com.github.scilldev.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings implements DataFile {

	private final TieredChat plugin;
	private FileConfiguration config;

	// mysql stuff
	private String mysqlHost;
	private int mysqlPort;
	private String mysqlDatabase;
	private String mysqlUsername;
	private String mysqlPassword;

	// other
	private String chatFormat;

	public Settings(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void reload() {
		config = plugin.getConfig();

		// mysql
		mysqlHost = _getMysqlHost();
		mysqlPort = _getMysqlPort();
		mysqlDatabase = _getMysqlDatabase();
		mysqlUsername = _getMysqlUsername();
		mysqlPassword = _getMysqlPassword();

		// other
		chatFormat = _getChatFormat();
	}

	private String _getChatFormat() {
		return Utils.color(config.getString("chat-format"));
	}

	public String getChatFormat() {
		return chatFormat;
	}

	private String _getMysqlHost() {
		return config.getString("mysql.host");
	}

	public String getMysqlHost() {
		return mysqlHost;
	}

	private int _getMysqlPort() {
		return config.getInt("mysql.port");
	}

	public int getMysqlPort() {
		return mysqlPort;
	}

	private String _getMysqlDatabase() {
		return config.getString("mysql.database");
	}

	public String getMysqlDatabase() {
		return mysqlDatabase;
	}

	private String _getMysqlUsername() {
		return config.getString("mysql.username");
	}

	public String getMysqlUsername() {
		return mysqlUsername;
	}

	private String _getMysqlPassword() {
		return config.getString("mysql.password");
	}

	public String getMysqlPassword() {
		return mysqlPassword;
	}
}
