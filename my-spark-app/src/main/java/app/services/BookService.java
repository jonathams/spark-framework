package app.services;

import java.sql.Connection;

import app.entity.Book;

public interface BookService {
	
	Book findByISBN(Integer isbn);

	String getAll();

	Book addBook(Book book);

	Book updateBook(Book book);
	
	void setConn(Connection conn);

	boolean deleteBook(Integer isbn);
}
