//package org.lsh.jpademo.service;
//
//import org.lsh.jpademo.domain.Board;
//import org.lsh.jpademo.dto.BoardDTO;
//import org.lsh.jpademo.dto.BoardListReplyCountDTO;
//import org.lsh.jpademo.dto.PageRequestDTO;
//import org.lsh.jpademo.dto.PageResponseDTO;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public interface BoardService1 {
//    PageResponseDTO<BoardDTO> getList(PageRequestDTO requestDTO);
//    Board getBoard(Long bno);
//    void saveBoard(Board board);
//    void updateBoard(Board board);
//    void deleteBoard(Long bno);
//    void visitCount(Long bno);
//    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
//    default Board dtoToEntity(BoardDTO boardDTO) {
//        Board board = Board.builder()
//                .bno(boardDTO.getBno())
//                .title(boardDTO.getTitle())
//                .content(boardDTO.getContent())
//                .writer(boardDTO.getWriter())
//                .build();
//        if (boardDTO.getFileNames() != null) {
//            boardDTO.getFileNames().forEach(fileName -> {
//                String[] arr = fileName.split("_");
//                board.addImage(arr[0], arr[1]);
//            });
//        }
//        return board;
//    }
//    default BoardDTO entityToDTO(Board board) {
//        BoardDTO boardDTO = BoardDTO.builder()
//                .bno(board.getBno())
//                .title(board.getTitle())
//                .content(board.getContent())
//                .writer(board.getWriter())
//                .regDate(board.getRegDate())
//                .modDate(board.getModDate())
//                .build();
//        List<String> fileNames =
//                board.getImageSet().stream().sorted().map(boardImage ->
//                                boardImage.getUuid()+"_"+boardImage.getFileName())
//                        .collect(Collectors.toList());
//        boardDTO.setFileNames(fileNames);
//        return boardDTO;
//    }
//}
