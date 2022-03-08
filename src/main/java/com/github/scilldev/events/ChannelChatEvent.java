package com.github.scilldev.events;

import com.github.scilldev.chat.ChatUser;
import com.github.scilldev.chat.channel.ChatChannel;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class ChannelChatEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final ChatChannel channel;
	private final ChatUser user;
	private final String message;
	private final Set<ChatUser> recipients;

	public ChannelChatEvent(ChatChannel channel, ChatUser user, String message, Set<ChatUser> recipients) {
		this.channel = channel;
		this.user = user;
		this.message = message;
		this.recipients = recipients;
	}

	public ChatChannel getChannel() {
		return channel;
	}

	public ChatUser getUser() {
		return user;
	}

	public String getMessage() {
		return message;
	}

	public Set<ChatUser> getRecipients() {
		return recipients;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
