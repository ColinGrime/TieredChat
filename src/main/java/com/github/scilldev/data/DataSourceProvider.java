package com.github.scilldev.data;

import com.github.scilldev.data.yaml.Settings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProvider {

	private final DataSource source;

	public DataSourceProvider(Settings settings) {
		this.source = init(settings);
	}

	private HikariDataSource init(Settings settings) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://"
				+ settings.getMysqlHost() + ":"
				+ settings.getMysqlPort() + "/"
				+ settings.getMysqlDatabase());
		config.setUsername(settings.getMysqlUsername());
		config.setPassword(settings.getMysqlPassword());
		config.setMaximumPoolSize(10);

		return new HikariDataSource(config);
	}

	public boolean testConection() {
		try (Connection conn = source.getConnection()) {
			return conn.isValid(5);
		} catch (SQLException throwables) {
			return false;
		}
	}

	public DataSource getSource() {
		return source;
	}

	public void close() {
		((HikariDataSource) source).close();
	}
}
