package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.model.domain.Board;
import com.hanghae0705.sbmoney.model.domain.Comment;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.BoardRepository;
import com.hanghae0705.sbmoney.repository.CommentRepository;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.security.SecurityUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, @Lazy BoardRepository boardRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    // 댓글 조회
    public Message getCommentList(Long BoardId) {
        List<Comment> commentList = commentRepository.findByBoard_Id(BoardId);
        List<Comment.Response> responseList = new ArrayList<>();
        for (Comment comment : commentList) {
            Comment.Response response = new Comment.Response(comment);
            responseList.add(response);
        }
        return new Message(true, "댓글을 조회했습니다.", responseList);
    }

    @Transactional
    public Message postComment(Long boardId, Comment.Request request) {
        checkValueIsEmpty(request.getComment());
        checkCommentLength(request.getComment());
        Comment comment = new Comment(request, (Board) getValueByIdFromRepo("board", boardId), getUser());
        commentRepository.save(comment);
        return new Message(true, "댓글을 등록하였습니다.");
    }

    @Transactional
    public Message updateComment(Comment.Request request, Long commentId, Long boardId) {
        getValueByIdFromRepo("board", boardId);
        Comment comment = (Comment) getValueByIdFromRepo("comment", commentId);
        checkCommentUserAndCurrentUser(comment);
        checkValueIsEmpty(request.getComment());
        checkCommentLength(request.getComment());
        comment.updateComment(request);
        return new Message(true, "댓글을 수정하였습니다.");
    }

    @Transactional
    public Message deleteComment(Long boardId, Long commentId){
        getValueByIdFromRepo("board", boardId);
        checkCommentUserAndCurrentUser((Comment) getValueByIdFromRepo("comment", commentId));
        commentRepository.deleteById(commentId);
        return new Message(true, "댓글을 삭제하였습니다.");
    }

    public User getUser() {
        return userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(
                () -> new ApiRequestException(ApiException.NOT_EXIST_USER));
    }

    public void checkCommentLength(String target) {
        if(target.length() > 200) {
            throw new IllegalArgumentException("댓글은 200자 내외로 작성해야합니다");
        }
    }

    public void checkCommentUserAndCurrentUser(Comment comment) {
        Comment target = (Comment) getValueByIdFromRepo("comment", comment.getId());
        if(!target.getUser().equals(getUser())){
            throw new ApiRequestException(ApiException.NOT_MATCH_USER);
        }
    }

    public void checkValueIsEmpty(String target) {
        if (target.trim().isEmpty()) {
            throw new NullPointerException("내용이 없음");
        }
    }

    // AOP를 적용하면 얘를 다른 곳에서 쓸 수 있게 한다 이건데...
    public Object getValueByIdFromRepo(String repo, Long id) {
        switch (repo) {
            case "comment":
                return commentRepository.findById(id).orElseThrow(
                        () -> new ApiRequestException(ApiException.NOT_EXIST_DATA));
            case "board":
                return boardRepository.findById(id).orElseThrow(
                        () -> new ApiRequestException(ApiException.NOT_EXIST_DATA));
        }
        return false;
    }
}