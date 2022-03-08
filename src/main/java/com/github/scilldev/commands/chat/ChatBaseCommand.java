package com.github.scilldev.commands.chat;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.BaseCommand;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ChatBaseCommand extends BaseCommand {

	public ChatBaseCommand(TieredChat plugin) {
		super(plugin, "chat");
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Messages.INVALID_SENDER.sendTo(sender);
			return;
		}

		SubCommand subCommand = getSubCommand(args[0]);
		String permission = subCommand.getPermission();

		if (permission != null && !sender.hasPermission(permission)) {
			Messages.INVALID_PERMISSION.sendTo(sender);
			return;
		}

		subCommand.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
	}

	@Override
	public Messages getUsage() {
		return Messages.CHAT_USAGE;
	}
}
