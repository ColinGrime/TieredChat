package com.github.scilldev.data.mysql.user;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.ChatUser;
import com.github.scilldev.chat.channel.ChatChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDataAbstraction implements UserData {

	private final TieredChat plugin;
	private final DataSource source;

	public UserDataAbstraction(TieredChat plugin) {
		this.plugin = plugin;
		this.source = plugin.getDataSourceProvider().getSource();
	}

	@Override
	public int loadUsers() {
		int loadedUsers = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (loadUser(player.getUniqueId())) loadedUsers++;
		}
		return loadedUsers;
	}

	@Override
	public boolean loadUser(UUID uuid) {
		// already loaded
		if (plugin.getChatManager().getUsers().containsKey(uuid)) {
			return false;
		}

		// load data if it exists
		if (exists(uuid)) {
			try (Connection conn = source.getConnection();
				 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tieredchat_users WHERE uuid=?;")) {
				stmt.setString(1, uuid.toString());
				ResultSet resultSet = stmt.executeQuery();

				if (resultSet.next()) {
					String channelName = resultSet.getString("channel_preference");
					ChatChannel channel = plugin.getChatManager().getChannelByName(channelName);
					ChatUser user = new ChatUser(uuid, channel, getFilteredMessages(uuid));
					plugin.getChatManager().getUsers().put(uuid, user);
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// insert default data
		else {
			try (Connection conn = source.getConnection();
				 PreparedStatement stmt = conn.prepareStatement("INSERT INTO tieredchat_users (uuid, channel_preference) VALUES (?, ?);")) {
				stmt.setString(1, uuid.toString());
				stmt.setString(2, plugin.getChatManager().getDefaultChannel().getName());
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	private List<String> getFilteredMessages(UUID uuid) {
		List<String> filterMessages = new ArrayList<>();

		try (Connection conn = source.getConnection();
			 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tieredchat_filter WHERE uuid=?;")) {
			stmt.setString(1, uuid.toString());
			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				filterMessages.add(resultSet.getString("message"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return filterMessages;
	}

	@Override
	public int saveUsers() {
		int savedUsers = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (saveUser(player.getUniqueId())) savedUsers++;
		}
		return savedUsers;
	}

	@Override
	public boolean saveUser(UUID uuid) {
		ChatUser user = plugin.getChatManager().getUsers().get(uuid);

		try (Connection conn = source.getConnection();
			 PreparedStatement stmt = conn.prepareStatement("UPDATE tieredchat_users SET channel_preference=? WHERE uuid=?;")) {
			stmt.setString(1, user.getChannelPreference().getName());
			stmt.setString(2, uuid.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		saveFilteredMessages(uuid);
		return true;
	}

	private void saveFilteredMessages(UUID uuid) {
		ChatUser user = plugin.getChatManager().getUsers().get(uuid);

		try (Connection conn = source.getConnection();
			 PreparedStatement delete = conn.prepareStatement("DELETE FROM tieredchat_filter WHERE uuid=?;");
			 PreparedStatement create = conn.prepareStatement("INSERT INTO tieredchat_filter (uuid, message) VALUES (?, ?);")) {
			delete.setString(1, uuid.toString());
			delete.executeUpdate();

			for (String message : user.getFilteredMessages()) {
				create.setString(1, uuid.toString());
				create.setString(2, message);
				create.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean exists(UUID uuid) {
		try (Connection conn = source.getConnection();
			 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tieredchat_users WHERE uuid=?;")) {
			stmt.setString(1, uuid.toString());
			return stmt.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
