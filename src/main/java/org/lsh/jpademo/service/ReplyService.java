package org.lsh.jpademo.service;

import org.lsh.jpademo.dto.PageRequestDTO;
import org.lsh.jpademo.dto.PageResponseDTO;
import org.lsh.jpademo.dto.ReplyDTO;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);
    ReplyDTO findById(Long rno);
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
