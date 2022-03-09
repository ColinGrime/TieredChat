package com.github.scilldev.events;

import com.github.scilldev.chat.channel.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class ChannelChatEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final ChatChannel channel;
	private final Player player;
	private final String message;
	private final Set<Player> recipients;

	public ChannelChatEvent(ChatChannel channel, Player player, String message, Set<Player> recipients) {
		this.channel = channel;
		this.player = player;
		this.message = message;
		this.recipients = recipients;
	}

	public ChatChannel getChannel() {
		return channel;
	}

	public Player getPlayer() {
		return player;
	}

	public String getMessage() {
		return message;
	}

	public Set<Player> getRecipients() {
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
