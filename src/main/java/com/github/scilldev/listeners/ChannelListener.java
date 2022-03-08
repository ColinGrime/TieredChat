package com.github.scilldev.listeners;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.ChatUser;
import com.github.scilldev.events.ChannelChatEvent;
import com.github.scilldev.utils.Logger;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class ChannelListener implements Listener {

	private final boolean DEBUG = true;
	private final TieredChat plugin;

	public ChannelListener(TieredChat plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onChannelChatEvent(ChannelChatEvent event) {
		if (DEBUG) {
			Logger.log("[Debug] Channel: " + event.getChannel().getName());
			Logger.log("[Debug] Player: " + event.getUser().getPlayer().getName());
			Logger.log("[Debug] Message: " + event.getMessage());
			Logger.log("[Debug] Recipients: " + event.getRecipients().stream().map(u -> u.getPlayer().getName()).collect(Collectors.joining(", ")));
		}

		Set<ChatUser> recipients = event.getRecipients();
		removeIfFiltered(event.getMessage(), recipients);

		// TODO hook into PlaceholderAPI and add your own placeholders
		String chatFormat = PlaceholderAPI.setPlaceholders(event.getUser().getPlayer(), plugin.getSettings().getChatFormat());
		chatFormat = chatFormat.replace("%channel%", event.getChannel().getName());
		chatFormat = chatFormat.replace("%message%", event.getMessage());

		event.getUser().getPlayer().sendMessage(chatFormat);
		for (ChatUser recipient : event.getRecipients()) {
			recipient.getPlayer().sendMessage(chatFormat);
		}
	}

	private void removeIfFiltered(String message, Set<ChatUser> users) {
		Iterator<ChatUser> usersIterator = users.iterator();

		while (usersIterator.hasNext()) {
			for (String filter : usersIterator.next().getFilteredMessages()) {
				if (message.contains(filter)) {
					usersIterator.remove();
					break;
				}
			}
		}
	}
}
