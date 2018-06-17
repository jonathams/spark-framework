package app.controller;

import static app.utils.JsonUtil.dataToJson;
import static app.utils.JsonUtil.readProperty;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.entity.Book;
import spark.Request;
import spark.Response;
import spark.Route;

public class BookController {
	/* Map holding the books */
	private static Map<String, Book> books = new HashMap<String, Book>();

	// get all books
	public static Route getAllBooks = (request, response) -> {
		if (books != null && books.size() > 0) {
			response.type("application/json");
			return dataToJson(books);
		} else {
			return "Sorry, No book was found";
		}
	};

	// Gets the book resource for the provided isbn
	public static Route getBookByIsbn = (Request request, Response response) -> {
		Book book = books.get(request.params(":isbn"));
		if (book != null) {
			return dataToJson(book);
		} else {
			response.status(404);
			return "Book not found";
		}
	};

	public static Route addBook = (request, response) -> {
		ObjectMapper mapper = new ObjectMapper();
		Book book = new Book();
		book = mapper.readValue(request.body(), Book.class);

		books.put(String.valueOf(book.getIsbn()), book);

		response.status(201);
		return "Book successfully saved";
	};

	// update the book
	public static Route updateBook = (request, response) -> {
		String isbn = request.params(":isbn");
		Book book = books.get(isbn);
		if (book != null) {
			String newAuthor = readProperty("author", request.body());
			String newTitle = readProperty("title", request.body());
			if (newAuthor != null) {
				book.setAuthor(newAuthor);
			}
			if (newTitle != null) {
				book.setTitle(newTitle);
			}
			return "Book with isbn '" + isbn + "' updated";
		} else {
			response.status(404);
			return "Book not found";
		}
	};

	public static Route deleteBook = (request, response) -> {
		String isbn = request.params(":isbn");

		Book book = books.get(isbn);
		if (book != null) {
			books.remove(book.getIsbn(), book);
			return "Book with isbn " + isbn + " removed!";
		} else {
			response.status(404);
			return "Book not found";
		}
	};

}
