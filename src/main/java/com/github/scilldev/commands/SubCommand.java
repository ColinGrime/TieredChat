package com.github.scilldev.commands;

import com.github.scilldev.data.yaml.Messages;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public interface SubCommand {

	void onCommand(CommandSender sender, String subCommand, String[] args);

	default ArrayList<String> onTabComplete(CommandSender sender, String subCommand, String[] args) {
		return null;
	}

	String getName();

	Messages getUsage();

	default String getPermission() {
		return null;
	}

	default String[] getAliases() {
		return new String[]{};
	}

	default boolean requireArguments() {
		return false;
	}
}
