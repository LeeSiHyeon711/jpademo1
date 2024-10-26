package org.lsh.jpademo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.jpademo.dto.BoardDTO;
import org.lsh.jpademo.dto.PageRequestDTO;
import org.lsh.jpademo.dto.PageResponseDTO;
import org.lsh.jpademo.service.BoardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    @Value("${org.lsh.jpademo.upload.path}")
    private String uploadPath;

    private final BoardService boardService;

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
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board POST register.......");
        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        }
        log.info(boardDTO);
        Long bno  = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
        BoardDTO boardDTO = boardService.readOne(bno, true); // 방문 수 증가
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
    }
    @GetMapping("/modify")
    public void modify(Long bno, PageRequestDTO pageRequestDTO, Model model){
        BoardDTO boardDTO = boardService.readOne(bno, false); //방문 수 증가하지 않음
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
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
