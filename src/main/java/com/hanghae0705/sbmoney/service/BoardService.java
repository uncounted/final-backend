package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Board;
import com.hanghae0705.sbmoney.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Message GetBoard (){
        List<Board> boardList =boardRepository.findAll();
        List<Board.Response> responseList = new ArrayList<>();
        for (Board board:boardList){
            Board.Response response = new Board.Response(board);
            responseList.add(response);
        }
        return new Message(true,"게시판을 조회하였습니다.",responseList);
    }

    public Message postBoard(Board.Request request){
        

    }


}
