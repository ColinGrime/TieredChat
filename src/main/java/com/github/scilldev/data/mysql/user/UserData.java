package com.github.scilldev.data.mysql.user;

import java.util.UUID;

public interface UserData {

	int loadUsers();

	boolean loadUser(UUID uuid);

	int saveUsers();

	boolean saveUser(UUID uuid);

	boolean exists(UUID uuid);
}
