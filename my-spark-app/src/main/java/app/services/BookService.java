package app.services;

import java.util.List;

import app.entity.Book;

public interface BookService {
	
	Book findByISBN(Integer isbn);

	List<Book> getAll();

	Book addBook(Book book);

	Book updateBook(Book book);

	boolean deleteBook(Integer isbn);
}
