package com.github.scilldev.commands.filter.subcommands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.yaml.Messages;
import com.github.scilldev.utils.Replacer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FilterAddSubCommand implements SubCommand {

	private final TieredChat plugin;

	public FilterAddSubCommand(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		List<String> filter = plugin.getChatManager().getUser((Player) sender).getFilteredMessages();
		Replacer replacer = new Replacer("%word%", args[0]);

		if (filter.contains(args[0])) {
			Messages.FAILURE_ALREADY_ADDED.sendTo(sender, replacer);
			filter.remove(args[0]);
		} else {
			filter.add(args[0]);
			Messages.SUCCESS_FILTER_ADD.sendTo(sender, replacer);
		}
	}

	@Override
	public String getName() {
		return "add";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_FILTER_ADD;
	}

	@Override
	public boolean requireArguments() {
		return true;
	}
}
