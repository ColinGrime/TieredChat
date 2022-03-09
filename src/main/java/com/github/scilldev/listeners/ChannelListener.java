package com.github.scilldev.listeners;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.ChatUser;
import com.github.scilldev.events.ChannelChatEvent;
import com.github.scilldev.utils.Logger;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class ChannelListener implements Listener {

	private final boolean DEBUG = false;
	private final TieredChat plugin;

	public ChannelListener(TieredChat plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onChannelChatEvent(ChannelChatEvent event) {
		if (DEBUG) {
			Logger.log("[Debug] Channel: " + event.getChannel().getName());
			Logger.log("[Debug] Player: " + event.getPlayer().getName());
			Logger.log("[Debug] Message: " + event.getMessage());

			String recipients = event.getRecipients().stream().map(HumanEntity::getName).collect(Collectors.joining(", "));
			Logger.log("[Debug] Recipients: " + (recipients.isEmpty() ? "None" : recipients));
		}

		Set<Player> recipients = event.getRecipients();
		removeIfFiltered(event.getMessage(), recipients);

		// TODO hook into PlaceholderAPI and add your own placeholders
		String chatFormat = PlaceholderAPI.setPlaceholders(event.getPlayer(), plugin.getSettings().getChatFormat());
		chatFormat = chatFormat.replace("%channel%", event.getChannel().getName());
		chatFormat = chatFormat.replace("%message%", event.getMessage());

		event.getPlayer().sendMessage(chatFormat);
		for (Player recipient : event.getRecipients()) {
			recipient.sendMessage(chatFormat);
		}
	}

	private void removeIfFiltered(String message, Set<Player> players) {
		Iterator<Player> playerIterator = players.iterator();

		while (playerIterator.hasNext()) {
			ChatUser user = plugin.getChatManager().getUser(playerIterator.next());
			for (String filter : user.getFilteredMessages()) {
				if (message.contains(filter)) {
					playerIterator.remove();
					break;
				}
			}
		}
	}
}
