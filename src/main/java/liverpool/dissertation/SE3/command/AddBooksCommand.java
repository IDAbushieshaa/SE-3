package liverpool.dissertation.SE3.command;

import java.util.ArrayList;
import java.util.List;

import liverpool.dissertation.SE3.entity.remote.Book;

public class AddBooksCommand {
	
	private List<Book> books = new ArrayList<>();

	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
