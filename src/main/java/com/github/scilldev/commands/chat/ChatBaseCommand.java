package com.github.scilldev.commands.chat;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.BaseCommand;
import com.github.scilldev.data.Messages;

public final class ChatBaseCommand extends BaseCommand {

	public ChatBaseCommand(TieredChat plugin) {
		super(plugin, "chat");
	}

	@Override
	public Messages getUsage() {
		return Messages.CHAT_USAGE;
	}

	@Override
	public boolean allowConsole() {
		return false;
	}
}
