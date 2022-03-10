package com.github.scilldev.chat.manager;

import com.github.scilldev.chat.ChatUser;
import com.github.scilldev.chat.channel.ChatChannel;
import com.github.scilldev.data.yaml.ChannelDataFile;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChatManager {

	/**
	 * Loads the channels when {@link ChannelDataFile#reload()} is called.
	 * @param channels newly loaded channels
	 */
	void loadChannels(List<ChatChannel> channels);

	/**
	 * Loads a channel when created todo in some command
	 * @param channel newly created channel
	 */
	void loadChannel(ChatChannel channel);

	/**
	 * @return the available channels
	 */
	List<ChatChannel> getChannels();

	/**
	 * @param channelName name of a channel
	 * @return ChatChannel object corresponding with the name
	 */
	ChatChannel getChannelByName(String channelName);

	/**
	 * @return default channel loaded in {@link ChannelDataFile#reload()}
	 */
	ChatChannel getDefaultChannel();

	/**
	 * @param defaultChannel default channel
	 */
	void setDefaultChannel(ChatChannel defaultChannel);

	/**
	 * TODO Loads the channels when *MySQL database method* is called.
	 * @param chatUsers newly loaded chat users
	 */
	void loadUsers(Map<UUID, ChatUser> chatUsers);

	/**
	 * @return map of player UUID's and their ChatUser object
	 */
	Map<UUID, ChatUser> getUsers();

	/**
	 * Gets the player's ChatUser implementation.
	 * @param player any player
	 * @return ChatUser object of player
	 */
	ChatUser getUser(Player player);
}
