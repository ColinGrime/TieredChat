package com.github.scilldev.commands.filter;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.BaseCommand;
import com.github.scilldev.data.yaml.Messages;

public class FilterBaseCommand extends BaseCommand {

	public FilterBaseCommand(TieredChat plugin) {
		super(plugin, "filter");
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_FILTER_COMMAND;
	}

	@Override
	public boolean allowConsole() {
		return false;
	}
}
