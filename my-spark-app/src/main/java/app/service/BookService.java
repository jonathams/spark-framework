package app.service;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import app.entity.Book;

public class BookService {
	/*Map holding the books*/
	private static Map<String, Book> books = new HashMap<String, Book>();
	
	public static void main(String[] args) {
		final Random random = new Random();
		System.out.println("bookservice");
		post("/books", (request, response) ->{
			String author = request.queryParams("author");
			String title = request.queryParams("title");
			String isbn = request.queryParams("isbn");
			
			Book book = new Book(author, title, isbn);
			
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
		
		
			
	}

}
