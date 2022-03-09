package com.github.scilldev.commands.filter.subcommands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FilterAddSubCommand implements SubCommand {

	private final TieredChat plugin;

	public FilterAddSubCommand(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		Player player = (Player) sender;
		plugin.getChatManager().getUser(player).getFilteredMessages().add(args[0]);
		// TODO add success message
	}

	@Override
	public String getName() {
		return "add";
	}

	@Override
	public Messages getUsage() {
		// TODO usage
		return null;
	}

	@Override
	public boolean requireArguments() {
		return true;
	}
}
