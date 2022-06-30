package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Board;
import com.hanghae0705.sbmoney.model.domain.Like;
import com.hanghae0705.sbmoney.repository.BoardRepository;
import com.hanghae0705.sbmoney.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;



    @Transactional
    public Message changeLike(Long boardId){
        Board board = boardRepository.findAllById(boardId);
        if(likeRepository.findByBoard(board).equals(null)){
            Like like = new Like(board);
            likeRepository.save(like);


        }else {
            Like like = likeRepository.findByBoard(board);
            if (like.isLike()==true){
                like.changeLike(false);
            }else {
                like.changeLike(true);
            }

        }
        Long likeCount = likeCount(boardId);
        boolean checkLike = checkLike(boardId);
        board.likeBoard(checkLike,likeCount);
        Like.Response response = new Like.Response(likeCount,checkLike);
        return new Message(true, "좋아요를 변경하였습니다.", response);

    }

    @Transactional
    public boolean checkLike(Long boardId){
        Board board = boardRepository.findAllById(boardId);
        if(likeRepository.findByBoard(board).equals(null)){
            return false;
        }else {
            Like like = likeRepository.findByBoard(board);
            return like.isLike();
        }
    }

    @Transactional
    public Long likeCount(Long boardId){
        Board board = boardRepository.findAllById(boardId);
        return (long) likeRepository.findAllByBoard(board).size();
    }


}
