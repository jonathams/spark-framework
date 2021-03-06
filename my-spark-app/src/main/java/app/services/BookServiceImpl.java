package app.services;

import static app.jooq.Tables.AUTHOR;
import static app.jooq.Tables.BOOKS;
import static app.utils.Constants.DBPASSWORD;
import static app.utils.Constants.DBURL;
import static app.utils.Constants.DBUSER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.JSONFormat;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import app.entity.Book;

public class BookServiceImpl implements BookService {

	AuthorService authorService = new AuthorServiceImpl();

	private Connection conn;
	
	public BookServiceImpl(){}

	@Override
	public Book findByISBN(Integer isbn) {
		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			Book book = new Book();

			DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL);

			Record result = dslContext.select()
									  .from(BOOKS)
									  .where("BOOKS.ISBN = " + isbn)
									  .fetchOne();

			if (result != null) {
				book.setIsbn(result.get(BOOKS.ISBN));
				book.setTitle(result.get(BOOKS.TITLE));
				book.setAuthor(dslContext.select()
										 .from(AUTHOR)
										 .where("ID = " + result.get(BOOKS.AUTHOR_ID))
										 .fetchOne()
										 .get(AUTHOR.NAME));
			}

			dslContext.close();

			return book;
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Book addBook(Book book) {

		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL);

			Record resultAuthor = dslContext.select()
											.from(AUTHOR)
											.where("NAME = '" + book.getAuthor() + "'")
											.fetchOne();
			if (resultAuthor != null) {
				dslContext.insertInto(BOOKS, BOOKS.ISBN, BOOKS.AUTHOR_ID, BOOKS.TITLE)
						  .values(book.getIsbn(), resultAuthor.get(AUTHOR.ID), book.getTitle()).execute();
				// dslContext.insertInto(BOOKS, BOOKS.ISBN, BOOKS.AUTHOR_ID, BOOKS.TITLE)
				// .values(book.getIsbn(), dslContext.select().from(AUTHOR)
				// .where("NAME = '" + book.getAuthor() + "'").fetchOne().get(AUTHOR.ID),
				// book.getTitle())
				// .execute();
			}

			dslContext.close();

			return book;
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Book updateBook(Book book) {

		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL);

			Record result = dslContext.select()
									  .from(BOOKS)
									  .where("BOOKS.ISBN = " + book.getIsbn())
									  .fetchOne();

			if (result != null) {
				result.set(BOOKS.AUTHOR_ID, dslContext.select()
													  .from(AUTHOR)
													  .where("NAME = '" + book.getAuthor() + "'")
													  .fetchOne()
													  .get(AUTHOR.ID));
				result.set(BOOKS.TITLE, book.getTitle());

				dslContext.update(BOOKS).set(result)
										.where("BOOKS.ISBN = " + book.getIsbn())
										.execute();
			}

			dslContext.close();

			return book;
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAll() {
		try {
			DSLContext dslContext = DSL.using(this.getConn(), SQLDialect.MYSQL);
			
			String result = dslContext.select().from(BOOKS).fetch().formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);
			
			dslContext.close();
			
			return result;
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Sorry, No book was found";

	}

	@Override
	public boolean deleteBook(Integer isbn) {
		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL);
			Record result = dslContext.select().from(BOOKS).where("BOOKS.ISBN = " + isbn).fetchOne();

			if (result == null) {
				dslContext.close();
				return false;
			}

			dslContext.deleteFrom(BOOKS).where("BOOKS.ISBN = " + isbn).execute();
			dslContext.close();

			return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	
}
