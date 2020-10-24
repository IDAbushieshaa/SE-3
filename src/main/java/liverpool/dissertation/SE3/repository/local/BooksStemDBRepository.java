package liverpool.dissertation.SE3.repository.local;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import liverpool.dissertation.SE3.entity.local.BookLocal;
import liverpool.dissertation.SE3.entity.remote.Book;

@Repository
public interface BooksStemDBRepository extends JpaRepository<BookLocal, Long>{
	List<BookLocal> findByTitleStem(String titleStem);
}
