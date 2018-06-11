import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import entity.Book;
import service.BookService;
public class Main {

	/*Map holding the books*/
	private static Map<String, Book> books = new HashMap<String, Book>();
	
	public static void main(String[] args) {
		
		final Random random = new Random();
		
		post("/books", (request, response) ->{
			String author = request.queryParams("author");
			String title = request.queryParams("title");
			
			Book book = new Book(author, title);
			
			int id = random.nextInt(Integer.MAX_VALUE);
			books.put(String.valueOf(id), book);
			
			response.status(201);
			return id;
			
		});
		
		// Gets the book resource for the provided id
        get("/books/:id", (request, response) -> {
            Book book = books.get(request.params(":id"));
            if (book != null) {
                return "Title: " + book.getTitle() + ", Author: " + book.getAuthor();
            } else {
                response.status(404); // 404 Not found
                return "Book not found";
            }
        });
		
		
		//port(8080);
        //simple get
		get("/hello", (req, res) -> "Hello World");
		
		//simple post
		post("/hello", (request, response) -> 
				"Hello World " + request.body()
		);		
		
		//simple get with response status
		get("private", (request, response) -> {
			response.status(401);
			return "Go Away!";
		});
		
		//simple get with path param
		get("/users/:name", (request,response) -> "Selected user: " + request.params(":name"));
		
		//simple get with path param that returns a xml text
		get("/news/:section", (request, response) ->{
			response.type("text/xml");
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>";
		});
		
		//simple get with path param that returns a json 
		get("/news/json/:section", (request, response) ->{
			response.type("application/json");
			return "{ news:" + request.params("section") + " }";
		});
		
		//simple get that returns a forbidden message
		get("protected", (request, response) -> {
			halt(403, "No way man!");
			return null;
		});
		
		//simple redirect
		get("/redirect", (request, response) ->{
			response.redirect("/news/json/world");
			return null;
		});
		
		//root
		get("/", (request, response) -> "root");
		
		
				
	}
}
