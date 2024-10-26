package org.lsh.jpademo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.jpademo.domain.Board;
import org.lsh.jpademo.dto.BoardDTO;
import org.lsh.jpademo.dto.PageRequestDTO;
import org.lsh.jpademo.dto.PageResponseDTO;
import org.lsh.jpademo.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO boardDTO) {
        //Board board = modelMapper.map(boardDTO, Board.class);
        Board board = dtoToEntity(boardDTO);
        Long bno = boardRepository.save(board).getBno();
        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno, boolean updateVisitCount) {
        //Optional<Board> result = boardRepository.findById(bno);
        Optional<Board> result = boardRepository.findByIdWithImages(bno);
        Board board = result.orElseThrow();
        //BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

        // 방문 수 증가 조건 추가
        if (updateVisitCount) {
            board.UpdateVisitCount();
            boardRepository.save(board); // 방문 수 업데이트 반영
        }
        BoardDTO boardDTO= entityToDTO(board);
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();
        board.change(boardDTO.getTitle(), boardDTO.getContent(), boardDTO.getWriter());
        board.clearImages();
        if (boardDTO.getFileNames() != null) {
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public void visitCount(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + bno));
        board.UpdateVisitCount();
        boardRepository.save(board);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.searchAll(types,keyword, pageable);
//        Page<Board> result=null;
//        Page<Board> result = boardRepository.searchAll(keyword, pageable);
//        if(types==null|| types.length<1) {
//            result = boardRepository.findAll(pageable);
//        }else{
//            result = boardRepository.searchAll(keyword, pageable);
//        }
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class))
                .collect(Collectors.toList());
        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
