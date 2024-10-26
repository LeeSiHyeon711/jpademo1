package org.lsh.jpademo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.jpademo.dto.PageRequestDTO;
import org.lsh.jpademo.dto.PageResponseDTO;
import org.lsh.jpademo.dto.ReplyDTO;
import org.lsh.jpademo.service.ReplyService;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    //댓글 등록
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(
            @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult)throws BindException {
        log.info(replyDTO);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        Map<String, Long> resultMap = new HashMap<>();
        Long rno = replyService.register(replyDTO);
        resultMap.put("rno",rno);
        return resultMap;
    }
    //게시물번호(bno)에 따른 댓글 조회
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                              PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
        return responseDTO;
    }
    //댓글번호(rno)에 따른 댓글 조회
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {
        ReplyDTO replyDTO = replyService.findById(rno);
        return replyDTO;
    }
    //댓글 삭제
    @DeleteMapping("/{rno}")
    public Map<String,Long> remove(@PathVariable("rno") Long rno) {
        replyService.remove(rno);
        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);
        return resultMap;
    }
    //댓글 수정
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> remove(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {
        replyDTO.setRno(rno);
        replyService.modify(replyDTO);
        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);
        return resultMap;
    }
}
