package com.github.scilldev.commands.filter;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.BaseCommand;
import com.github.scilldev.data.Messages;

public class FilterBaseCommand extends BaseCommand {

	public FilterBaseCommand(TieredChat plugin) {
		super(plugin, "filter");
	}

	@Override
	public Messages getUsage() {
		// TODO usage
		return null;
	}

	@Override
	public boolean allowConsole() {
		return false;
	}
}
