package com.github.scilldev.data.yaml;

import com.github.scilldev.TieredChat;
import com.github.scilldev.chat.channel.ChatChannel;
import com.github.scilldev.chat.channel.ChatChannelAbstraction;
import com.github.scilldev.chat.channel.DisplayType;
import com.github.scilldev.data.DataFile;
import com.github.scilldev.utils.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChannelData implements DataFile {

	private final TieredChat plugin;
	private final File file;
	private FileConfiguration config;

	public ChannelData(TieredChat plugin) {
		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder(), "channels.yml");
		init();
	}

	private void init() {
		// creates the channels.yml file if it doesn't exist
		if (!file.exists()) {
			if (file.getParentFile().mkdirs()) {
				Logger.log("TieredChat directory has been created.");
			}

			plugin.saveResource("channels.yml", false);
		}
	}

	@Override
	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection channelSec = config.getConfigurationSection("channels");

		if (channelSec == null) {
			Logger.log("There are no channels to load!");
			return;
		}

		plugin.getChatManager().loadChannels(getChannels(channelSec));
	}

	private List<ChatChannel> getChannels(ConfigurationSection channelSec) {
		List<ChatChannel> channels = new ArrayList<>();
		for (String channelName : channelSec.getKeys(false)) {
			// options: commands, permission, display-type, block-radius
			List<String> commands = channelSec.getStringList(channelName + ".commands");
			String permission = Optional.ofNullable(channelSec.getString(channelName + ".permission")).orElse("");
			DisplayType display = Optional.ofNullable(DisplayType.getByName(channelSec.getString(channelName + ".display-type"))).orElse(DisplayType.GLOBAL);
			int blockRadius = channelSec.getInt(channelName + ".block-radius");

			// creates and adds the channel
			ChatChannel channel = new ChatChannelAbstraction(channelName, commands, permission, display, blockRadius);
			channels.add(channel);

			// set it as default if configured to it
			if (channelName.equalsIgnoreCase(config.getString("default-channel"))) {
				plugin.getChatManager().setDefaultChannel(channel);
			}
		}

		return channels;
	}
}
