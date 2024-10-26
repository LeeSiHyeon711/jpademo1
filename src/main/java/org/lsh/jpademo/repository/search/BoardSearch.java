package org.lsh.jpademo.repository.search;

import org.lsh.jpademo.domain.Board;
import org.lsh.jpademo.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
//    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
//                                                      String keyword,
//                                                      Pageable pageable);
}
