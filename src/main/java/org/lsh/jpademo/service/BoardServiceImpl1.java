//package org.lsh.jpademo.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.lsh.jpademo.domain.Board;
//import org.lsh.jpademo.dto.BoardDTO;
//import org.lsh.jpademo.dto.BoardListReplyCountDTO;
//import org.lsh.jpademo.dto.PageRequestDTO;
//import org.lsh.jpademo.dto.PageResponseDTO;
//import org.lsh.jpademo.repository.BoardRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Log4j2
//@RequiredArgsConstructor
//public class BoardServiceImpl1 implements BoardService1 {
//    private final BoardRepository boardRepository;
//    private final ModelMapper modelMapper;
//    @Override
//    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
//        log.info("getList");
////        Pageable pageable=pageRequestDTO.getPageable("bno");
////        Page<Board> result = boardRepository.findAll(pageable);
////        Page<Board> result=null;
////        if(pageRequestDTO.getKeyword()==null || pageRequestDTO.getKeyword().equals("")) {
////            result =boardRepository.findAll(pageable);
////        }else {
////            result = boardRepository.searchAll(pageRequestDTO.getKeyword(), pageable);
////        }
////        log.info("aaaa"+result.getTotalElements());
//        String[] types = pageRequestDTO.getTypes();
//        String keyword = pageRequestDTO.getKeyword();
//        Pageable pageable = pageRequestDTO.getPageable("bno");
//        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
//        List<BoardDTO> dtoList=result.getContent().stream()
//                .map(board -> modelMapper.map(board, BoardDTO.class))
//                .collect(Collectors.toUnmodifiableList());
//
//        return PageResponseDTO.<BoardDTO>withAll()
//                .pageRequestDTO(pageRequestDTO)
//                .dtoList(dtoList)
//                .total((int)result.getTotalElements())
//                .build();
//    }
//
//    @Override
//    public Board getBoard(Long bno) {
//        Board board = boardRepository.findById(bno).get();
//        board.UpdateVisitCount();
//        boardRepository.save(board);
//        return board;
//    }
//
//    @Override
//    public void saveBoard(Board board) {
//        boardRepository.save(board);
//    }
//
//    @Override
//    public void updateBoard(Board board) {
//        Board oldBoard = boardRepository.findById(board.getBno()).get();
//        oldBoard.setTitle(board.getTitle());
//        oldBoard.setWriter(board.getWriter());
//        oldBoard.setContent(board.getContent());
//        boardRepository.save(oldBoard);
//    }
//
//    @Override
//    public void deleteBoard(Long bno) {
//        boardRepository.deleteById(bno);
//    }
//
//    @Override
//    public void visitCount(Long bno) {
//        Board board = boardRepository.findById(bno)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + bno));
//        board.UpdateVisitCount();
//        boardRepository.save(board);
//    }
//
//    @Override
//    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
//        String[] types = pageRequestDTO.getTypes();
//        String keyword = pageRequestDTO.getKeyword();
//        Pageable pageable = pageRequestDTO.getPageable("bno");
//
//        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
//
//        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
//                .pageRequestDTO(pageRequestDTO)
//                .dtoList(result.getContent())
//                .total((int)result.getTotalElements())
//                .build();
//    }
//}
