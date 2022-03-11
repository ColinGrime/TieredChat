package com.github.scilldev.data;

import com.github.scilldev.data.yaml.Settings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProvider {

	private final HikariDataSource source;

	public DataSourceProvider(Settings settings) {
		this.source = new HikariDataSource();
		init(settings);
	}

	private void init(Settings settings) {
		source.setJdbcUrl("jdbc:mysql://"
				+ settings.getMysqlHost() + ":"
				+ settings.getMysqlPort() + "/"
				+ settings.getMysqlDatabase());
		source.setUsername(settings.getMysqlUsername());
		source.setPassword(settings.getMysqlPassword());
		source.setMaximumPoolSize(10);
	}

	public boolean testConection() {
		try (Connection conn = source.getConnection()) {
			return conn.isValid(5);
		} catch (SQLException e) {
			return false;
		}
	}

	public DataSource getSource() {
		return source;
	}

	public void close() {
		source.close();
	}
}
