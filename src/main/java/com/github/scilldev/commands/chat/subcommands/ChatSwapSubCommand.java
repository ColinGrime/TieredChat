package com.github.scilldev.commands.chat.subcommands;

import com.github.scilldev.TieredChat;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.data.Messages;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ChatSwapSubCommand implements SubCommand {

	private final TieredChat plugin;

	public ChatSwapSubCommand(TieredChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {

	}

	@Override
	public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
		return SubCommand.super.onTabComplete(sender, args);
	}

	@Override
	public String getName() {
		return "change";
	}

	@Override
	public Messages getUsage() {
		return null;
	}

	@Override
	public String getPermission() {
		return "tieredchat.swap";
	}

	@Override
	public String[] getAliases() {
//		List<String> channelNames = new ArrayList<>();
//		for (ChatChannelAbstraction channel : main.getChatManager().getChannels()) {
//			channel.get
//		}
//
//		return new String[]{"change", }
		return null;
	}
}
