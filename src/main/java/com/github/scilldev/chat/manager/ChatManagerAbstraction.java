package com.github.scilldev.chat.manager;

import com.github.scilldev.chat.ChatUser;
import com.github.scilldev.chat.channel.ChatChannel;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatManagerAbstraction implements ChatManager {

	private List<ChatChannel> channels = new ArrayList<>();
	private Map<UUID, ChatUser> chatUsers = new HashMap<>();
	private ChatChannel defaultChannel;

	@Override
	public void loadChannels(List<ChatChannel> channels) {
		this.channels = channels;
	}

	@Override
	public void loadChannel(ChatChannel channel) {
		this.channels.add(channel);
	}

	@Override
	public List<ChatChannel> getChannels() {
		return channels;
	}

	@Override
	public ChatChannel getDefaultChannel() {
		return defaultChannel;
	}

	@Override
	public void setDefaultChannel(ChatChannel defaultChannel) {
		this.defaultChannel = defaultChannel;
	}

	@Override
	public Map<UUID, ChatUser> getUsers() {
		return chatUsers;
	}

	@Override
	public ChatUser getUser(Player player) {
		UUID uuid = player.getUniqueId();
		if (!chatUsers.containsKey(uuid)) {
			chatUsers.put(uuid, new ChatUser(uuid, defaultChannel));
		}

		return chatUsers.get(uuid);
	}
}
