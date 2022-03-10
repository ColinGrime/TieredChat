package com.github.scilldev.chat;

import com.github.scilldev.chat.channel.ChatChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatUser {

	private final UUID uuid;

	private ChatChannel channelPreference;
	private List<String> filteredMessages;

	public ChatUser(UUID uuid, ChatChannel channelPreference) {
		this(uuid, channelPreference, new ArrayList<>());
	}

	public ChatUser(UUID uuid, ChatChannel channelPreference, List<String> filteredMessages) {
		this.uuid = uuid;
		this.channelPreference = channelPreference;
		this.filteredMessages = filteredMessages;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public ChatChannel getChannelPreference() {
		return channelPreference;
	}

	public void setChannelPreference(ChatChannel channelPreference) {
		this.channelPreference = channelPreference;
	}

	public List<String> getFilteredMessages() {
		return filteredMessages;
	}
}
