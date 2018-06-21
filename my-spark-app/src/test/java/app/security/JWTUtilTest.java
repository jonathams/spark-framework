package app.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

class JWTUtilTest {

	@Test
	void testJwtUtil() {
		JWTUtil jwtUtil = new JWTUtil();
		
		String username = "UserTest";
		
		String jwtToken = jwtUtil.create(username);
		
		Jws<Claims> jws = jwtUtil.decode(jwtToken);
		
		assertEquals(username, jws.getBody().getSubject());
	}

}