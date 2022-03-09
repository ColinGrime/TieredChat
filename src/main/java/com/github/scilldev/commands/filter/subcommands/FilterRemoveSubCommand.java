package com.github.scilldev.commands.filter.subcommands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FilterRemoveSubCommand implements SubCommand {

	private final TieredChat plugin;

	public FilterRemoveSubCommand(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		Player player = (Player) sender;
		List<String> filter = plugin.getChatManager().getUser(player).getFilteredMessages();

		if (filter.contains(args[0])) {
			filter.remove(args[0]);
			// TODO sucess message
		} else {
			// TODO doesn't exist message
		}
	}

	@Override
	public String getName() {
		return "remove";
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
