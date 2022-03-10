package com.github.scilldev.commands.filter.subcommands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.yaml.Messages;
import com.github.scilldev.utils.Replacer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FilterRemoveSubCommand implements SubCommand {

	private final TieredChat plugin;

	public FilterRemoveSubCommand(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		List<String> filter = plugin.getChatManager().getUser((Player) sender).getFilteredMessages();

		if (filter.contains(args[0])) {
			filter.remove(args[0]);
			Messages.SUCCESS_FILTER_REMOVE.sendTo(sender, new Replacer("%word%", args[0]));
		} else {
			// TODO doesn't exist message
		}
	}

	@Override
	public ArrayList<String> onTabComplete(CommandSender sender, String subCommand, String[] args) {
		if (args.length == 1) {
			Player player = (Player) sender;
			return (ArrayList<String>) plugin.getChatManager().getUser(player).getFilteredMessages();
		}

		return null;
	}

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_FILTER_REMOVE;
	}

	@Override
	public boolean requireArguments() {
		return true;
	}
}
