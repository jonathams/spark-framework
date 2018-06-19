package app.services;

import app.entity.User;

public interface UserService {

	User findByUserName(String username);
	
}
