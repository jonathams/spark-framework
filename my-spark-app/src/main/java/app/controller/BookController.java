package app.controller;

import static app.utils.JsonUtil.dataToJson;
import static app.utils.JsonUtil.readProperty;
import static org.jooq.impl.DSL.*;
import static app.jooq.generated.Tables.*;
import static app.utils.Constants.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.exception.DataAccessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.entity.Book;
import spark.Request;
import spark.Response;
import spark.Route;

public class BookController {
	/* Map holding the books */
	private static Map<Integer, Book> books = new HashMap<Integer, Book>();

	// get all books
	public static Route getAllBooks = (request, response) -> {

		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<Record> result = create.select().from(BOOKS).fetch();

			books.clear();

			for (Record r : result) {
				Book book = new Book(r.getValue(BOOKS.AUTHOR), r.getValue(BOOKS.TITLE), r.getValue(BOOKS.ISBN));
				books.put(book.getIsbn(), book);
			}
			create.close();
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (books != null && books.size() > 0) {
			response.type("application/json");
			return dataToJson(books);
		} else {
			return "Sorry, No book was found";
		}
	};

	// Gets the book resource for the provided isbn
	public static Route getBookByIsbn = (Request request, Response response) -> {

		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Record result = create.select().from(BOOKS).where("BOOKS.ISBN = " + request.params(":isbn")).fetchOne();

			Book book = new Book(result.getValue(BOOKS.AUTHOR), result.getValue(BOOKS.TITLE),
					result.getValue(BOOKS.ISBN));
			create.close();

			if (book != null) {
				return dataToJson(book);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.status(404);
		return "Book not found";
	};

	public static Route addBook = (request, response) -> {

		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			ObjectMapper mapper = new ObjectMapper();
			Book book = new Book();
			book = mapper.readValue(request.body(), Book.class);

			books.put(book.getIsbn(), book);

			create.insertInto(BOOKS, BOOKS.ISBN, BOOKS.AUTHOR, BOOKS.TITLE)
					.values(book.getIsbn(), book.getAuthor(), book.getTitle()).execute();

			create.close();

			response.status(201);
			return "Books succesfully saved!";
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.status(401);
		return "Error inserting book";
	};

	// update the book
	public static Route updateBook = (request, response) -> {

		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Record result = create.select().from(BOOKS).where("BOOKS.ISBN = " + request.params(":isbn")).fetchOne();

			if (result != null) {
				result.set(BOOKS.AUTHOR, readProperty("author", request.body()));
				result.set(BOOKS.TITLE, readProperty("title", request.body()));

				create.update(BOOKS).set(result).where("BOOKS.ISBN = " + request.params(":isbn")).execute();
			}

			create.close();

			return "Book updated";
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.status(404);
		return "Book not found";
	};

	public static Route deleteBook = (request, response) -> {
		try (Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD)) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Record result = create.select().from(BOOKS).where("BOOKS.ISBN = " + request.params(":isbn")).fetchOne();

			if (result != null) {
				create.deleteFrom(BOOKS).where("BOOKS.ISBN = " + request.params(":isbn")).execute();
			}

			create.close();

			return "Book removed";
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.status(404);
		return "Book not found";
	};

}
