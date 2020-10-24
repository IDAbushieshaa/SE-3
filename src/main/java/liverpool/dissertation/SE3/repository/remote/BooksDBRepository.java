package liverpool.dissertation.SE3.repository.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import liverpool.dissertation.SE3.entity.remote.Book;

@Repository
public interface BooksDBRepository extends JpaRepository<Book, Long>{
}

