package org.lsh.jpademo.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.lsh.jpademo.domain.Board;
import org.lsh.jpademo.dto.BoardDTO;
import org.lsh.jpademo.dto.upload.UploadFileDTO;
import org.lsh.jpademo.dto.upload.UploadResultDTO;
import org.lsh.jpademo.service.BoardService;
import org.lsh.jpademo.service.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/upload")
public class UpDownController {
    private final BoardServiceImpl boardServiceImpl;
    private final BoardService boardService;
    @Value("${org.lsh.jpademo.upload.path}")
    private String uploadPath;

    public UpDownController(BoardServiceImpl boardServiceImpl, BoardService boardService) {
        this.boardServiceImpl = boardServiceImpl;
        this.boardService = boardService;
    }

    @GetMapping("/uploadForm")
    public void uploadForm(){
        log.info("uploadForm");
    }

    @PostMapping(value = "/uploadPro", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPro(UploadResultDTO uploadResultDTO,UploadFileDTO uploadFileDTO, BoardDTO boardDTO, Model model){
        log.info("uploadResultDTO : "+uploadResultDTO);
        log.info(uploadFileDTO);
        log.info(boardDTO);

        if (uploadFileDTO.getFiles()!=null){
            final List<UploadResultDTO> list = new ArrayList<>();
            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);
                log.info(list);

                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);
                boolean image = false;
                try {
                    multipartFile.transferTo(savePath);
                    if (Files.probeContentType(savePath).startsWith("image")){
                        image = true;
                        String thumbnailFileName = "s_"+ uuid + "_" + originalName;
                        File thumbFile = new File(uploadPath, thumbnailFileName);
                        Thumbnailator.createThumbnail(savePath.toFile(),thumbFile,200,200);
                    }
                }catch (IOException e){e.printStackTrace();}
                list.add(UploadResultDTO.builder()
                                .uuid(uuid)
                                .fileName(originalName)
                                .image(image)
                                .build()
                );
                model.addAttribute("list",list);
                model.addAttribute("uploadPath",uploadPath);
            });
            BoardDTO board = boardServiceImpl.saveBoardWithImages(boardDTO, list);
            model.addAttribute("board",board);
        }
        return "redirect:/board/list";
    }

    @GetMapping("/view/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @GetMapping("/remove")
    public String removeFile(@RequestParam("fileName") String fileName){
        log.info(fileName);
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();
        Map<String,Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if (contentType.startsWith("image")){
                String fileName1 = fileName.replace("s_","");
                File originalFile = new File(uploadPath+File.separator+fileName1);
                originalFile.delete();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "redirect:/upload/uploadForm";
    }
}
