package com.github.scilldev.commands.filter.subcommands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.yaml.Messages;
import com.github.scilldev.utils.Replacer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FilterListSubCommand implements SubCommand {

	private final TieredChat plugin;

	public FilterListSubCommand(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		List<String> filter = plugin.getChatManager().getUser((Player) sender).getFilteredMessages();
		String filterMessage = String.join(", ", filter);

		Messages.SUCCESS_FILTER_LIST.sendTo(sender, new Replacer("%words%", filterMessage));
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_FILTER_LIST;
	}
}
