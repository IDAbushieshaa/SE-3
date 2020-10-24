package liverpool.dissertation.SE3.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import liverpool.dissertation.SE3.encryption.AES;
import liverpool.dissertation.SE3.entity.local.BookLocal;
import liverpool.dissertation.SE3.entity.remote.Book;
import liverpool.dissertation.SE3.repository.local.BooksStemDBRepository;
import liverpool.dissertation.SE3.repository.remote.BooksDBRepository;

@Service
public class BooksServiceImpl implements BooksService{
	
	
	@Autowired
	private BooksDBRepository booksDBRepository;
	@Autowired
	private BooksStemDBRepository booksLocalDBRepository;
	

	@Override
	public List<BookLocal> insertBooks(List<Book> books) {
		int begin = 0;
		int end = 10;
		int numberIdexed = 0;
		List<BookLocal> inserted = new ArrayList<BookLocal>();
		
		while(end <= books.size()) {
			List<Book> subList = books.subList(begin, end);
			List<BookLocal> insertedBooks = insertBooksInRemoteDBWithTitleEncrypted(subList);
			
			System.out.println("Inserted the book list " + insertedBooks);
			
			inserted.addAll(insertedBooks);
			numberIdexed += insertInLocalBookStemInLocalDatabase(insertedBooks);
			
			begin += 10;
			end += 10;
			
			try {
				Thread.sleep(2000);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return inserted;
	}
	
	private List<BookLocal> insertBooksInRemoteDBWithTitleEncrypted(List<Book> books) {
		
		List<BookLocal> localBooks = new ArrayList<BookLocal>();
		for(Book book : books) {
			Book encryptedBook = new Book();
			String encryptionIV = AES.generateEncryptionIV();
			byte[] encryptionIVBytes = Base64.getDecoder().decode(encryptionIV);
			encryptedBook.setTitle(AES.encrypt(book.getTitle(), encryptionIVBytes));
			Book insertedBook = booksDBRepository.save(encryptedBook);
			BookLocal localBook = new BookLocal();
			localBook.setDatabaseId(insertedBook.getId());
			localBook.setEncryptionIV(encryptionIV);
			localBook.setTitleStem(analyzeText(book.getTitle()));
			localBooks.add(localBook);
		}
		return localBooks;
	}
	
	private int insertInLocalBookStemInLocalDatabase(List<BookLocal> insertedBooks) {
		if(insertedBooks == null || insertedBooks.size() == 0)
			return 0;
		
		int numInserted = 0;
		for(BookLocal book : insertedBooks) {
			booksLocalDBRepository.save(book);
			numInserted++;
		}
		return numInserted;
	}
	
	private String analyzeText(String title) {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		TokenStream stream = analyzer.tokenStream(null, title);
		stream = new StandardFilter(stream);
		String analyzedTitle = "";
		try {
			stream.reset();
			while(stream.incrementToken()) {
				String token = stream.getAttribute(CharTermAttribute.class).toString();
				token = token.replace("'", "");
				analyzedTitle += token;
				analyzedTitle += " ";
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return analyzedTitle;
	}

	
	@Autowired
	@Qualifier("SecondDS")
	DriverManagerDataSource secondDataSource;
	
	@Override
	public List<Book> findBooksByTitle(String title, int pageSize) {
		
		String analyzedTitle = analyzeText(title);
		
		String SQL = "Select DATABASEID, TITLESTEM, ENCRYPTIONIV, MATCH(titleStem) AGAINST ('" + analyzedTitle.trim() + "' IN NATURAL LANGUAGE MODE) AS SCORE "
				+ "FROM BookLocal"
				 + " Where MATCH(titleStem) AGAINST ('" + analyzedTitle.trim() + "' IN NATURAL LANGUAGE MODE) > 0";
		ResultSet rs = null;
		Map<Long, String> remoteDatabaseIDs = new HashMap<Long, String>();
		try {
			Statement statement = secondDataSource.getConnection().createStatement();
			rs = statement.executeQuery(SQL);
			while(rs.next()) {
				remoteDatabaseIDs.put(rs.getLong("DATABASEID"), rs.getString("ENCRYPTIONIV"));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		List<Book> books = booksDBRepository.findAllById(remoteDatabaseIDs.keySet());
		for(Book book : books) {
			byte[] iv = Base64.getDecoder().decode(remoteDatabaseIDs.get(book.getId()));
			book.setTitle(AES.decrypt(book.getTitle(), iv));
		}
		return books;
	}
}
