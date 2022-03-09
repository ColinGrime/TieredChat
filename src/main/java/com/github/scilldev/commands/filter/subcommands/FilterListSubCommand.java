package com.github.scilldev.commands.filter.subcommands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.Messages;
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
		Player player = (Player) sender;
		List<String> filter = plugin.getChatManager().getUser(player).getFilteredMessages();

		// TODO filter message in config
	}

	@Override
	public String getName() {
		return "list";
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
