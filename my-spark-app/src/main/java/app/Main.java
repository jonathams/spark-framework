package app;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import app.controller.BookController;
import app.controller.LoginController;
import app.utils.Path;
import spark.Request;
import spark.Response;

public class Main {

	public static void main(String[] args) {

		// {"username":"jonatha","password":"password"}
		post(Path.LOGIN, (req, res) -> {
			LoginController loginController = new LoginController();
			Response response = loginController.login(req, res);
			return response.body();
		});

		before(Path.Books.BOOKS, (req, res) -> {
			verifyAuth(req, res);
		});

		before(Path.Books.SINGLE_BOOK, (req, res) -> {
			verifyAuth(req, res);
		});

		get(Path.Books.BOOKS, (req, res) -> {
			BookController bookController = new BookController();
			Response response = bookController.getAllBooks(req, res);
			return response.body();
		});

		get(Path.Books.SINGLE_BOOK, (req, res) -> {
			BookController bookController = new BookController();
			Response response = bookController.getBookByIsbn(req, res);
			return response.body();
		});

		// {"author":"Stephen King","title":"It","isbn":"00933728929"}
		post(Path.Books.BOOKS, (req, res) -> {
			BookController bookController = new BookController();
			Response response = bookController.addBook(req, res);
			return response.body();
		});

		// {"author":"Stephen King","title":"Carrie","isbn":"00933728929"}
		put(Path.Books.SINGLE_BOOK, (req, res) -> {
			BookController bookController = new BookController();
			Response response = bookController.updateBook(req, res);
			return response.body();
		});

		delete(Path.Books.SINGLE_BOOK, (req, res) -> {
			BookController bookController = new BookController();
			Response response = bookController.deleteBook(req, res);
			return response.body();
		});

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

	private static void verifyAuth(Request req, Response res) {
		LoginController loginController = new LoginController();

		if (!loginController.verify(req, res)) {
			halt(401, "User not logged in or disconnected. Please login again");
		}
	}
}