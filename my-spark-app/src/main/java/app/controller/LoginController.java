package app.controller;

import static app.utils.JsonUtil.readProperty;

import app.entity.User;
import app.security.JWTUtil;
import app.services.UserService;
import app.services.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import spark.Request;
import spark.Response;

public class LoginController {

	JWTUtil jwtUtil = new JWTUtil();
	UserService userService = new UserServiceImpl();

	public Response login(Request request, Response response) {

		String userJson = request.body();

		User user = userService.findByUserName(readProperty("username", userJson));

		if (user != null && user.getPassword().equals(readProperty("password", userJson))
				&& user.getUsername().equals(readProperty("username", userJson))) {
			response.status(201);
			response.body(jwtUtil.create(user.getUsername()));
			return response;
		} else {
			response.status(401);
			response.body("Authentication error. Unable to login using User/Password provided.");
			return response;
		}
	};

	public boolean verify(Request request, Response response) {

		try {
			Jws<Claims> jws = jwtUtil.decode(request.headers("Authorization"));
			User user = userService.findByUserName(jws.getBody().getSubject());

			if (user != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}