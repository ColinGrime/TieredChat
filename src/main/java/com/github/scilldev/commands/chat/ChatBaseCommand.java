package com.github.scilldev.commands.chat;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.channel.ChatChannel;
import com.github.scilldev.commands.BaseCommand;
import com.github.scilldev.data.yaml.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class ChatBaseCommand extends BaseCommand {

	private final TieredChat plugin;

	public ChatBaseCommand(TieredChat plugin) {
		super(plugin, "chat");
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> channels = new ArrayList<>();
		for (ChatChannel channel : plugin.getChatManager().getChannels()) {
			if (channel.hasPermission((Player) sender)) {
				channels.add(channel.getName().toLowerCase());
			}
		}

		return channels;
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_CHAT_COMMAND;
	}

	@Override
	public boolean allowConsole() {
		return false;
	}
}
