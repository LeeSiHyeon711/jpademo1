package org.lsh.jpademo.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.lsh.jpademo.domain.Board;
import org.lsh.jpademo.domain.QBoard;
import org.lsh.jpademo.domain.QReply;
import org.lsh.jpademo.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
//        BooleanBuilder builder = new BooleanBuilder();
        if ((types != null && types.length > 0) && (keyword != null)) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        builder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        builder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(board.writer.contains(keyword));
                        break;
                }
            }
            query.where(builder);
        }
        query.where(board.bno.gt(0l));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(list, pageable, total);
    }

//    @Override
//    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//        JPQLQuery<Board> query = from(board);
//        query.leftJoin(reply).on(reply.board.eq(board));
//        query.groupBy(board);
//
//        if ( (types != null && types.length > 0) && keyword != null ){
//            BooleanBuilder booleanBuilder = new BooleanBuilder();
//            for(String type: types){
//                switch (type){
//                    case "t":
//                        booleanBuilder.or(board.title.contains(keyword));
//                        break;
//                    case "c":
//                        booleanBuilder.or(board.content.contains(keyword));
//                        break;
//                    case "w":
//                        booleanBuilder.or(board.writer.contains(keyword));
//                        break;
//                }
//            }
//            query.where(booleanBuilder);
//        }
//        query.where(board.bno.gt(0L));
//
//        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.
//                bean(BoardListReplyCountDTO.class,
//                        board.bno,
//                        board.title,
//                        board.writer,
//                        board.regDate,
//                        reply.count().as("replyCount")
//                ));
//
//        this.getQuerydsl().applyPagination(pageable, dtoQuery);
//        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
//        long count = dtoQuery.fetchCount();
//        return new PageImpl<>(dtoList, pageable, count);
//    }
}
