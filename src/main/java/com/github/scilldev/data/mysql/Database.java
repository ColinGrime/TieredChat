package com.github.scilldev.data.mysql;

public interface Database {

	void setupVersionTable();

	boolean checkForUpdates();

	void performUpdates();

	void init();

	void startTimers();
}
