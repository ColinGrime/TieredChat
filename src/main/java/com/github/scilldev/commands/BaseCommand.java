package com.github.scilldev.commands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.data.Messages;
import com.github.scilldev.utils.Logger;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseCommand implements CommandExecutor, TabExecutor {

	private final Map<String, SubCommand> subCommands = new HashMap<>();

	public BaseCommand(TieredChat plugin, String name) {
		PluginCommand command = plugin.getCommand(name);

		if (command == null) {
			Logger.severe("Commands have failed to load. Plugin has been disabled.");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0 || subCommands.get(args[0]) == null) {
			getUsage().sendTo(sender);
			return true;
		}

		onCommand(sender, args);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return new ArrayList<>(subCommands.keySet());
		}

		SubCommand subCommand = subCommands.get(args[0]);
		if (subCommand == null) {
			return null;
		}

		return subCommand.onTabComplete(sender, args);
	}

	public void registerSubCommand(SubCommand subCommand) {
		subCommands.put(subCommand.getName(), subCommand);

		if (subCommand.getAliases() != null) {
			for (String alias : subCommand.getAliases()) {
				subCommands.put(alias, subCommand);
			}
		}
	}

	public SubCommand getSubCommand(String subCommandString) {
		return subCommands.get(subCommandString);
	}

	public abstract void onCommand(CommandSender sender, String[] args);

	public abstract Messages getUsage();
}
