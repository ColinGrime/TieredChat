package com.github.scilldev.data.mysql;

import com.github.scilldev.utils.Logger;

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

public class Database {

	private final int latestVersion = 1;
	private final int latestPatch = 0;

	private final DataSource source;
	private int version, patch;

	public Database(DataSource source) {
		this.source = source;
		setupVersioning();
		getVersioning();
	}

	private void setupVersioning() {
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

	private void getVersioning() {
		boolean isVersionAvailable = false;
		try (Connection conn = source.getConnection();
			 PreparedStatement stmt = conn.prepareStatement("SELECT version, patch FROM tieredchat_version;")) {
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				version = resultSet.getInt("version");
				patch = resultSet.getInt("patch");
				isVersionAvailable = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!isVersionAvailable) {
			try (Connection conn = source.getConnection();
				 PreparedStatement stmt = conn.prepareStatement("INSERT INTO tieredchat_version(version, patch) VALUES(?, ?);")) {
				version = latestVersion;
				patch = latestPatch;
				stmt.setInt(1, version);
				stmt.setInt(2, patch);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		Logger.log("Database version: " + version + "." + patch);
	}

	public void init() {
		long time = System.currentTimeMillis();
		Logger.log("Initializing database...");

		for (String query : getQueries()) {
			if (query.isEmpty()) {
				continue;
			}

			try (Connection conn = source.getConnection();
				 PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		Logger.log("Database initialized in " + (System.currentTimeMillis() - time) + " ms");
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

	}
}
