package org.lsh.jpademo.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.lsh.jpademo.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {
//    @Autowired
//    private BoardRepository boardRepository;
//    @Test
//    public void testInsert() {
//        Board board = new Board();
//        board.setTitle("title1");
//        board.setWriter("user01");
//        board.setContent("content1");
//        boardRepository.save(board);
//        Board board1 = Board.builder()
//                .title("title2")
//                .writer("user01")
//                .content("content2")
//                .build();
//        boardRepository.save(board1);
//    }
//    @Test
//    public void getList(){
//        List<Board> list = boardRepository.findAll();
//        for (Board board : list) {
//            log.info(board);
//        }
//    }
//    @Test
//    public void selectOne(){
//        Board board = boardRepository.findById(1L).get();
//        log.info(board);
//    }
//    @Test
//    public void updateBoardTest(){
//        Board board = boardRepository.findById(1L).get();
//        board.setTitle("title3");
//        board.setContent("content3");
//        boardRepository.save(board);
//    }
//    @Test
//    public void deleteBoardTest(){
//        Board board = boardRepository.findById(1L).get();
//        boardRepository.delete(board);
//    }
}
