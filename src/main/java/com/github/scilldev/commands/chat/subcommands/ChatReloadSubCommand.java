package com.github.scilldev.commands.chat.subcommands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.yaml.Messages;
import org.bukkit.command.CommandSender;

public class ChatReloadSubCommand implements SubCommand {

	private final TieredChat plugin;

	public ChatReloadSubCommand(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		plugin.reload();
		Messages.RELOADED.sendTo(sender);
	}

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_CHAT_COMMAND;
	}

	@Override
	public String getPermission() {
		return "chat.reload";
	}
}
