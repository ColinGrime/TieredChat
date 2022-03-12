package com.github.scilldev.commands.chat;

import com.github.scilldev.chat.channel.ChatChannel;
import com.github.scilldev.data.yaml.Messages;
import com.github.scilldev.events.ChannelChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * This class is used to add additional chat channel
 * commands to the CommandMap at runtime.
 */
public class ChannelCommand extends Command {

	private final ChatChannel channel;

	public ChannelCommand(String command, ChatChannel channel) {
		super(command);
		this.channel = channel;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			Messages.INVALID_SENDER.sendTo(sender);
			return true;
		}

		Player player = (Player) sender;
		if (!channel.hasPermission(player)) {
			Messages.INVALID_PERMISSION.sendTo(player);
			return true;
		}

		// set channel
		if (args.length == 0) {
			player.performCommand("chat " + channel.getName());
			return true;
		}

		String message = String.join(" ", args);
		Set<Player> recipients = channel.getAllowedListeners(player);

		// quick channel chat
		ChannelChatEvent channelEvent = new ChannelChatEvent(channel, player, message, recipients);
		Bukkit.getServer().getPluginManager().callEvent(channelEvent);
		return true;
	}
}