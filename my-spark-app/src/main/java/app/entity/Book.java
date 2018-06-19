package app.entity;

public class Book {

	private String title;
	private int isbn;
	private String author;

	public Book() {

	}

	public Book(String author, String title, int isbn) {
		this.author = author;
		this.title = title;
		this.isbn = isbn;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

}
