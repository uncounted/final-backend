package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Board;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.BoardRepository;
import com.hanghae0705.sbmoney.repository.GoalItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final GoalItemRepository goalItemRepository;
    @Transactional
    public Message GetBoard (){
        List<Board> boardList =boardRepository.findAll();
        List<Board.Response> responseList = new ArrayList<>();
        for (Board board:boardList){
            Board.Response response = new Board.Response(board);
            responseList.add(response);
        }
        return new Message(true,"게시판을 조회하였습니다.",responseList);
    }
    @Transactional
    public Message postBoard(Board.Request request){
            User user = null;
            GoalItem goalItem = goalItemRepository.findAllById(request.getGoalItemId());
            Board board = new Board(request,goalItem,user);
            boardRepository.save(board);
            return new Message(true,"게시글을 등록하였습니다");
    }


}
