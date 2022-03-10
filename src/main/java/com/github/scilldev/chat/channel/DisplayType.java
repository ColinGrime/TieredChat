package com.github.scilldev.chat.channel;

import com.github.scilldev.utils.Logger;

public enum DisplayType {

	GLOBAL,
	LOCAL;

	public static DisplayType getByName(String name) {
		for (DisplayType display : DisplayType.values()) {
			if (display.name().equalsIgnoreCase(name)) {
				return display;
			}
		}

		// defaults to GLOBAL
		Logger.log("Display type \"" + name + "\" does not exist, defaulting to GLOBAL.");
		return GLOBAL;
	}
}
