package com.github.scilldev.commands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.data.yaml.Messages;
import com.github.scilldev.utils.Logger;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class BaseCommand implements CommandExecutor, TabExecutor {

	private final Map<String, SubCommand> subCommands = new HashMap<>();

	public BaseCommand(TieredChat plugin, String name) {
		PluginCommand command = plugin.getCommand(name);

		// disable plugin if a command is invalid
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
		// if there's no subcommand input, send the command usage
		if (args.length == 0 || subCommands.get(args[0]) == null) {
			getUsage().sendTo(sender);
			return true;
		}

		// if the console is both disabled and the sender, it's invalid
		if (!allowConsole() && !(sender instanceof Player)) {
			Messages.INVALID_SENDER.sendTo(sender);
			return true;
		}

		SubCommand subCommand = getSubCommand(args[0]);
		String permission = subCommand.getPermission();

		// sender doesn't have permission
		if (permission != null && !sender.hasPermission(permission)) {
			Messages.INVALID_PERMISSION.sendTo(sender);
			return true;
		}

		String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
		if (subCommand.requireArguments() && subArgs.length == 0) {
			subCommand.getUsage().sendTo(sender);
			return true;
		}

		// subcommand runs
		subCommand.onCommand(sender, args[0], subArgs);
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

		return subCommand.onTabComplete(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
	}

	public void registerSubCommand(SubCommand subCommand) {
		// register subcommand with name
		if (subCommand.getName() != null) {
			subCommands.put(subCommand.getName(), subCommand);
		}

		// register subcommand with aliases
		if (subCommand.getAliases() != null) {
			for (String alias : subCommand.getAliases()) {
				subCommands.put(alias, subCommand);
			}
		}
	}

	public SubCommand getSubCommand(String subCommandString) {
		return subCommands.get(subCommandString);
	}

	public abstract Messages getUsage();

	public abstract boolean allowConsole();
}
