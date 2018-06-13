package app;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.entity.Book;
import static app.utils.JsonUtil.*;

public class Main {

	/* Map holding the books */
	private static Map<String, Book> books = new HashMap<String, Book>();

	public static void main(String[] args) {

		post("/books", (request, response) -> {

			ObjectMapper mapper = new ObjectMapper();
			Book book = new Book();
			book = mapper.readValue(request.body(), Book.class);

			books.put(String.valueOf(book.getIsbn()), book);

			response.status(201);
			return "Book successfully saved";

		});

		// Gets the book resource for the provided isbn
		get("/books/:isbn", (request, response) -> {
			Book book = books.get(request.params(":isbn"));
			if (book != null) {
				return "Title: " + book.getTitle() + ", Author: " + book.getAuthor();
			} else {
				response.status(404); // 404 Not found
				return "Book not found";
			}
		});

		// updates the book for the provided id with the author and title send through
		// the query param
		put("/books/:isbn", (request, response) -> {
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
				response.status(404); // 404 Not found
				return "Book not found";
			}
		});

		// delete a book from the list based on the isbn received from the request
		delete("/books/:isbn", (request, response) -> {
			String isbn = request.params(":isbn");
			;
			Book book = books.get(isbn);
			if (book != null) {
				books.remove(book);
				return "Book with isbn " + isbn + " removed!";
			} else {
				response.status(404);
				return "Book not found";
			}
		});

		get("/books", (request, response) -> {
			if (books != null && books.size() > 0) {
				response.type("application/json");
				return dataToJson(books);
			} else {
				return "Sorry, No book was found";
			}
		});

		// port(8080);
		// simple get
		get("/hello", (req, res) -> "Hello World");

		// simple post
		post("/hello", (request, response) -> "Hello World " + request.body());

		// simple get with response status
		get("private", (request, response) -> {
			response.status(401);
			return "Go Away!";
		});

		// simple get with path param
		get("/users/:name", (request, response) -> "Selected user: " + request.params(":name"));

		// simple get with path param that returns a xml text
		get("/news/:section", (request, response) -> {
			response.type("text/xml");
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>";
		});

		// simple get with path param that returns a json
		get("/news/json/:section", (request, response) -> {
			response.type("application/json");
			return "{ news:" + request.params("section") + " }";
		});

		// simple get that returns a forbidden message
		get("protected", (request, response) -> {
			halt(403, "No way man!");
			return null;
		});

		// simple redirect
		get("/redirect", (request, response) -> {
			response.redirect("/news/json/world");
			return null;
		});

		// root
		get("/", (request, response) -> "root");

	}
}