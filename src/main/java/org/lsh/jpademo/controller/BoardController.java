package org.lsh.jpademo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.lsh.jpademo.dto.BoardDTO;
import org.lsh.jpademo.dto.PageRequestDTO;
import org.lsh.jpademo.dto.PageResponseDTO;
import org.lsh.jpademo.dto.upload.UploadFileDTO;
import org.lsh.jpademo.dto.upload.UploadResultDTO;
import org.lsh.jpademo.service.BoardService;
import org.lsh.jpademo.service.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardServiceImpl boardServiceImpl;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping(value = "/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               UploadFileDTO uploadFileDTO, Model model) {
        // 1. 바인딩 에러가 있는 경우, 등록 페이지로 리다이렉트
        if (bindingResult.hasErrors()) {
            log.info("Validation errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register";
        }
        // 2. 파일 리스트 초기화
        List<UploadResultDTO> list = null;
        // 3. 첨부 파일이 있는지 확인
        if (uploadFileDTO.getFiles() != null && !uploadFileDTO.getFiles().isEmpty()) {
            // 파일이 있는 경우에만 파일 처리 메서드 호출
            try {
                list = boardService.processUploadFiles(uploadFileDTO);
            } catch (Exception e) {
                log.error("File processing failed, redirecting to registration page.", e);
                redirectAttributes.addFlashAttribute("fileError", "File processing failed.");
                return "redirect:/board/register";
            }
        }
        // 4. 파일이 있을 경우에는 파일과 함께 게시글을 저장, 없을 경우 빈 리스트나 파일 관련 정보 초기화
        if (list != null && !list.isEmpty()) {
            boardServiceImpl.saveBoardWithImages(boardDTO, list);
        } else {
            // 파일이 없을 경우에는 boardDTO의 fileNames 필드를 명시적으로 null로 설정
            boardDTO.setFileNames(null);
            boardService.register(boardDTO);
        }
        // 5. 저장 완료 후 리스트 페이지로 리다이렉트
        redirectAttributes.addFlashAttribute("result", boardDTO.getBno());
        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
        BoardDTO boardDTO = boardService.readOne(bno, true); // 방문 수 증가
        log.info(boardDTO);
        List<UploadResultDTO> fileNames = boardDTO.getFileNames();
        model.addAttribute("dto", boardDTO);
        model.addAttribute("fileName", fileNames);
    }
    @GetMapping("/modify/{bno}")
    public String modify(@PathVariable Long bno, PageRequestDTO pageRequestDTO, Model model){
        BoardDTO boardDTO = boardService.readOne(bno, false); //방문 수 증가하지 않음
        log.info(boardDTO);
        List<UploadResultDTO> fileNames = boardDTO.getFileNames();
        model.addAttribute("dto", boardDTO);
        model.addAttribute("fileName", fileNames);
        return "board/modify";
    }
    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("board modify post......." + boardDTO);
        if (bindingResult.hasErrors()) {
            log.info("has errors....... : " + bindingResult.getAllErrors());
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?" + link;
        }
        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
    }
    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {
        log.info("remove post.. " + bno);
        boardService.remove(bno);
        redirectAttributes.addFlashAttribute("result", "removed");
        return "redirect:/board/list";
    }

}
