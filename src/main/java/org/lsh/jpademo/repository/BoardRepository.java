package org.lsh.jpademo.repository;

import org.lsh.jpademo.domain.Board;
import org.lsh.jpademo.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
//    @Query("select b from Board  b where b.title like concat('%',:keyword,'%') " +
//            "order by b.bno desc")
//    Page<Board> searchAll(String keyword, Pageable pageable);
//    Page<Board> findByTitle(String title, Pageable pageable);
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno=:bno")
    Optional<Board> findByIdWithImages(Long bno);
}
