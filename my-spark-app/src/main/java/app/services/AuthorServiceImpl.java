package app.services;

import static app.jooq.Tables.AUTHOR;
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

import app.entity.Author;

public class AuthorServiceImpl implements AuthorService {

	@Override
	public Author findById(Integer id) {
		Author author = new Author();
		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL);

			Record result = dslContext.select().from(AUTHOR).where("USER.ID = " + id).fetchOne();

			if (result != null) {
				author.setId(result.get(AUTHOR.ID));
				author.setName(result.get(AUTHOR.NAME));
			}

			dslContext.close();

		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return author;
	}

}
