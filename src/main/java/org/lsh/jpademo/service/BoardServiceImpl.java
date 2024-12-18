package org.lsh.jpademo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.lsh.jpademo.domain.Board;
import org.lsh.jpademo.domain.BoardImage;
import org.lsh.jpademo.dto.BoardDTO;
import org.lsh.jpademo.dto.PageRequestDTO;
import org.lsh.jpademo.dto.PageResponseDTO;
import org.lsh.jpademo.dto.upload.UploadFileDTO;
import org.lsh.jpademo.dto.upload.UploadResultDTO;
import org.lsh.jpademo.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    @Value("${org.lsh.jpademo.upload.path}")
    private String uploadPath;

    @Override
    public Long register(BoardDTO boardDTO) {
        //Board board = modelMapper.map(boardDTO, Board.class);
        Board board = dtoToEntity(boardDTO, null);
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
            for (UploadResultDTO fileDTO : boardDTO.getFileNames()) {
                String uuid = fileDTO.getUuid();
                String fileName = fileDTO.getFileName();
                board.addImage(uuid, fileName);
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
    public BoardDTO saveBoardWithImages(BoardDTO boardDTO, List<UploadResultDTO> uploadResultDTOList) {
        Board board = dtoToEntity(boardDTO, uploadResultDTOList);
        boardRepository.save(board);
        return boardDTO;
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

    @Override
    public List<UploadResultDTO> processUploadFiles(UploadFileDTO uploadFileDTO) {
        if (uploadFileDTO.getFiles() == null || uploadFileDTO.getFiles().isEmpty()) {
            return null; // 파일이 없을 때는 null 반환
        }
        List<UploadResultDTO> list = new ArrayList<>();
        uploadFileDTO.getFiles().forEach(multipartFile -> {
            try {
                String originalName = multipartFile.getOriginalFilename();
                // 파일 이름이 비어 있으면 처리하지 않음
                if (originalName == null || originalName.isEmpty()) {
                    return;
                }
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

                multipartFile.transferTo(savePath);
                boolean isImage = Files.probeContentType(savePath) != null && Files.probeContentType(savePath).startsWith("image");

                if (isImage) {
                    String thumbnailFileName = "s_" + uuid + "_" + originalName;
                    File thumbFile = new File(uploadPath, thumbnailFileName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                }

                list.add(new UploadResultDTO(uuid, originalName, isImage));
            } catch (IOException e) {
                log.error("File upload error: ", e);
            }
        });
        return list;
    }
    @Override
    public UploadResultDTO createUploadResultDTO(BoardImage boardImage) {
        String fullFileName = boardImage.getUuid() + "_" + boardImage.getFileName();
        boolean isImage = false;
        try {
            Path filePath = Paths.get(uploadPath, fullFileName);
            String contentType = Files.probeContentType(filePath);
            isImage = contentType != null && contentType.startsWith("image");
        }catch (IOException e) {
            log.error("File to determine content type", e);
        }
        return new UploadResultDTO(
                boardImage.getUuid(),
                boardImage.getFileName(),
                isImage
        );

    }
}
