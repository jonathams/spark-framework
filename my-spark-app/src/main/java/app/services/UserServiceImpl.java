package app.services;

import static app.jooq.Tables.USER;
import static app.utils.Constants.DBPASSWORD;
import static app.utils.Constants.DBURL;
import static app.utils.Constants.DBUSER;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import app.entity.User;
import app.exceptions.UserNotFoundException;

public class UserServiceImpl implements UserService {

	public UserServiceImpl() {
	}

	@Override
	public User findByUserName(String username) {
		User user = new User();
		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL);

			Record result = dslContext.select().from(USER).where("USER.USERNAME = '" + username + "'").fetchOne();

			if (result == null) {
				throw new UserNotFoundException("user not found");
			} else {
				user.setUsername(result.get(USER.USERNAME));
				user.setPassword(result.get(USER.PASSWORD));
				user.setName(result.get(USER.NAME));
			}

			dslContext.close();

		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

}
