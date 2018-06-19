package app.services;

import app.entity.Author;

public interface AuthorService {
	Author findById(Integer id);
}
