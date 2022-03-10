package com.github.scilldev.chat.channel;

import com.github.scilldev.chat.manager.ChatManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public interface ChatChannel {

	/**
	 * @return name of the channel
	 */
	String getName();

	/**
	 * Gets the optional commands players can use
	 * to talk in the channel without swapping to it.
	 *
	 * @return commands to talk in the channel
	 */
	List<String> getCommands();

	/**
	 * @return permission required to talk in the channel
	 */
	String getPermission();

	/**
	 * @return display type of the channel, either GLOBAL or LOCAL
	 */
	DisplayType getDisplayType();

	/**
	 * @return how far a player can be to "hear" other players
	 */
	double getBlockRadius();

	/**
	 * Gets the online players who have this channel
	 * selected as held in {@link ChatManager#getUser(Player)}.
	 *
	 * @return players that have this channel selected
	 */
	Set<Player> getCurrentListeners();

	/**
	 * Gets the players who are allowed to listen
	 * to {@code player} when they speak.
	 *
	 * @param player messenger
	 * @return allowed players
	 */
	Set<Player> getAllowedListeners(Player player);
}
