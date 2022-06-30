package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.model.domain.Comment;
import com.hanghae0705.sbmoney.repository.BoardRepository;
import com.hanghae0705.sbmoney.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    // 댓글 조회
    public Message getCommentList(Long BoardId) {

        checkBoardId(BoardId);

        List<Comment> commentList = commentRepository.findByBoard_Id(BoardId);

        List<Comment.Response> responseList = new ArrayList<>();

        for(Comment comment : commentList) {
            Comment.Response response = new Comment.Response(comment);
            responseList.add(response);
        }
        return new Message(true, "댓글을 조회했습니다.", responseList);
    }

    // 댓글 등록
    @Transactional
    public Message postComment(Comment.Request request) {

        checkValueIsEmpty(request.getComment());

        Comment comment = new Comment(request);
        commentRepository.save(comment);
        return new Message(true, "댓글을 등록하였습니다.");
    }

    // 댓글 수정
    @Transactional
    public Message updateComment(Comment.Request request) {
        checkValueIsEmpty(request.getComment());
        Comment comment = new Comment(request);
        comment.updateComment(request);
        commentRepository.save(comment);
        return new Message(true, "댓글을 수정하였습니다.");
    }


    public void checkBoardId(Long boardId){
        if(!boardRepository.findById(boardId).isPresent()){
            throw new NullPointerException("존재하지 않는 게시글");
        }
    }

    public void checkValueIsEmpty(String target) {
        if(target.trim().isEmpty()) {
            throw new NullPointerException("내용이 없음");
        }
    }
}
