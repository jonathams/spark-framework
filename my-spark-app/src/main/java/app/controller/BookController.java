package app.controller;

import static app.utils.JsonUtil.dataToJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.entity.Book;
import app.services.BookService;
import app.services.BookServiceImpl;
import spark.Request;
import spark.Response;

public class BookController {
	/* Map holding the books */
	private static List<Book> books = new ArrayList<Book>();

	BookService bookService = new BookServiceImpl();

	// get all books
	public Response getAllBooks(Request request, Response response) {

		books = bookService.getAll();

		if (books != null && books.size() > 0) {
			response.type("application/json");
			response.body(dataToJson(books));
		} else {
			response.body("Sorry, No book was found");
		}

		return response;
	};

	// Gets the book resource for the provided isbn
	public Response getBookByIsbn(Request request, Response response) {

		Book book = bookService.findByISBN(Integer.valueOf(request.params(":isbn")));

		if (book != null) {
			response.body(dataToJson(book));
		} else {
			response.status(404);
			response.body("Book not found");
		}
		return response;
	};

	public Response addBook(Request request, Response response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Book book = new Book();

			book = mapper.readValue(request.body(), Book.class);
			book = bookService.addBook(book);

			if (book != null) {
				response.status(201);
				response.body("Book succesfully saved!");
				return response;
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.status(401);
		response.body("Error saving book");
		return response;
	};

	// update the book
	public Response updateBook(Request request, Response response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Book book = new Book();

			book = mapper.readValue(request.body(), Book.class);
			book = bookService.updateBook(book);

			if (book != null) {
				response.status(201);
				response.body("Book succesfully saved!");
				return response;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.status(401);
		response.body("Error updating book");
		return response;

	};

	public Response deleteBook(Request request, Response response) {

		boolean isDeleted = bookService.deleteBook(Integer.valueOf(request.params(":isbn")));

		if (isDeleted) {
			response.status(201);
			response.body("Book deleted");
		} else {
			response.status(401);
			response.body("Book not found");
		}

		return response;
	};

}
