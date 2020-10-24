package liverpool.dissertation.SE3.service;

import java.util.List;

import liverpool.dissertation.SE3.entity.local.BookLocal;
import liverpool.dissertation.SE3.entity.remote.Book;

public interface BooksService {
	
	List<BookLocal> insertBooks(List<Book> books);
	
	List<Book> findBooksByTitle(String title, int pageSize);

}