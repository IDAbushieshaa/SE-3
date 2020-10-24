package liverpool.dissertation.SE3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import liverpool.dissertation.SE3.command.AddBooksCommand;
import liverpool.dissertation.SE3.command.FindBooksCommand;
import liverpool.dissertation.SE3.entity.local.BookLocal;
import liverpool.dissertation.SE3.entity.remote.Book;
import liverpool.dissertation.SE3.response.AddBooksResponse;
import liverpool.dissertation.SE3.response.FindBooksResponse;
import liverpool.dissertation.SE3.service.BooksService;

@RestController
@RequestMapping(path="/books")
public class BooksController {
	
	@Autowired
	BooksService booksService;
	
	@PostMapping(path= "/addBooks", consumes = "application/json", produces = "application/json")
	public AddBooksResponse addBooks(@RequestBody AddBooksCommand command) {
		
		List<Book> books = command.getBooks();
		
		List<BookLocal> insertedBooks = booksService.insertBooks(books);
		
		AddBooksResponse response = new AddBooksResponse();
		response.setSuccess(true);
		response.setStatus("200");
		return response;
	}
	
	@PostMapping(path = "/findBooksByTitle", consumes = "application/json", produces = "application/json")
	public  FindBooksResponse findBooks(@RequestBody FindBooksCommand command) {
		List<Book> result = booksService.findBooksByTitle(command.getSearchTerm(), 100);
		FindBooksResponse response = new FindBooksResponse();
		response.setCount(result.size());
		response.setBooks(result);
		response.setStatus("200");
		response.setSuccess(true);
		return response;
	}
}