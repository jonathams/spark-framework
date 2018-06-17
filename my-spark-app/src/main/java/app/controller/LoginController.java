package app.controller;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.entity.User;
import app.security.JWTUtil;
import app.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import spark.Request;
import spark.Response;

public class LoginController {

	JWTUtil jwtUtil = new JWTUtil();
	User user = new User();

	public Response login(Request request, Response response)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		user = mapper.readValue(request.body(), User.class);
		if (user.getPassword().equals(Constants.PASSWORD) && user.getUsername().equals(Constants.USERNAME)) {
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
			if (jws.getBody().getSubject().equals(Constants.USERNAME)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}
}