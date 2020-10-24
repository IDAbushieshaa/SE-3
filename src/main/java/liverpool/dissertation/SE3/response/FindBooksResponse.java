package liverpool.dissertation.SE3.response;

import java.util.ArrayList;
import java.util.List;

import liverpool.dissertation.SE3.entity.remote.Book;

public class FindBooksResponse {
	
	private boolean success;
	private String status;
	
	private int count;
	
	private List<Book> books = new ArrayList<Book>();

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
