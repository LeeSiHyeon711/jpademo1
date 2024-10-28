package org.lsh.jpademo.service;

import org.lsh.jpademo.domain.Board;
import org.lsh.jpademo.domain.BoardImage;
import org.lsh.jpademo.dto.BoardDTO;
import org.lsh.jpademo.dto.PageRequestDTO;
import org.lsh.jpademo.dto.PageResponseDTO;
import org.lsh.jpademo.dto.upload.UploadFileDTO;
import org.lsh.jpademo.dto.upload.UploadResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno, boolean updateVisitCount);
    void modify(BoardDTO boardDTO);
    void remove(Long bno);
    void visitCount(Long bno);
    BoardDTO saveBoardWithImages(BoardDTO boardDTO, List<UploadResultDTO> uploadResultDTOList);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    List<UploadResultDTO> processUploadFiles(UploadFileDTO uploadFileDTO);


    default Board dtoToEntity(BoardDTO boardDTO, List<UploadResultDTO> uploadResultDTOList) {
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .visitCount(boardDTO.getVisitCount())
                .build();
        if(uploadResultDTOList != null){
            uploadResultDTOList.forEach(uploadResultDTO -> {
                board.addImage(uploadResultDTO.getUuid(), uploadResultDTO.getFileName());
            });
        }
        return board;
    }
    default BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .visitCount(board.getVisitCount())
                .build();

        List<UploadResultDTO> fileNames =
                board.getImageSet().stream()
                        .sorted()
                        .map(this::createUploadResultDTO)
                        .collect(Collectors.toList());
        boardDTO.setFileNames(fileNames);
        return boardDTO;
    }

    UploadResultDTO createUploadResultDTO(BoardImage boardImage);
}
