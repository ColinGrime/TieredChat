package com.github.scilldev.data.mysql;

import com.github.scilldev.TieredChat;
import com.github.scilldev.utils.Logger;
import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DatabaseAbstraction implements Database {

	private final int latestVersion = 1;
	private final int latestPatch = 0;

	private final TieredChat plugin;
	private final DataSource source;

	private int version = 1, patch = 0;

	public DatabaseAbstraction(TieredChat plugin) {
		this.plugin = plugin;
		this.source = plugin.getDataSourceProvider().getSource();

		setupVersionTable();
		if (checkForUpdates()) {
			performUpdates();
		} else {
			Logger.log("Database is up-to-date (version " + version + "." + patch + ")");
		}

		init();
	}

	@Override
	public void setupVersionTable() {
		String versionTable = "CREATE TABLE IF NOT EXISTS tieredchat_version ("
				+ "version INT DEFAULT 1 NOT NULL,"
				+ "patch INT DEFAULT 0 NOT NULL"
				+ ");";

		try (Connection conn = source.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(versionTable)) {
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkForUpdates() {
		try (Connection conn = source.getConnection();
			 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tieredchat_version;")) {
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				version = resultSet.getInt("version");
				patch = resultSet.getInt("patch");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return !(version == latestVersion && patch == latestPatch);
	}

	// this is currently empty since there are no updates
	@Override
	public void performUpdates() {
		// while (patch < latestPatch);
		// while (version < latestVersion);
	}

	@Override
	public void init() {
		for (String query : getQueries()) {
			if (query.isEmpty()) continue;
			try (Connection conn = source.getConnection();
				 PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private String[] getQueries() {
		try (InputStream in = getClass().getResourceAsStream("/database/version_" + version + "/setup.sql")) {
			String query = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
			return query.split(";");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new String[]{};
	}

	public void startTimers() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
			Logger.log("Saving user data...");
			plugin.timeAction(() -> plugin.getUserData().saveUsers(), "Users saved in %s ms");
		}, 2 * 60 * 20L, 2 * 60 * 20L);
	}
}
