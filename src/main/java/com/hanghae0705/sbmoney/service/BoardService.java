package com.hanghae0705.sbmoney.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Board;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.BoardRepository;
import com.hanghae0705.sbmoney.repository.GoalItemRepository;
import com.hanghae0705.sbmoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.auth0.jwt.JWT.decode;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LikeService likeService;
    private final GoalItemRepository goalItemRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    private Optional<User> getUser(String authorization) {
        String token = authorization.substring(7);
        DecodedJWT decodeToken = decode(token);
        String username = decodeToken.getClaim("sub").toString();
        int length = username.length();
        username = username.substring(1, length - 1);
        Optional<User> user = userRepository.findByUsername(username);

        return user;

    }

    @Transactional
    public Message GetBoard(String authorization) {
        List<Board> boardList = boardRepository.findAll();
        List<Board.Response> responseList = new ArrayList<>();
        for (Board board : boardList) {
            if(authorization == null){
                Long likeCount = likeService.likeCount(board.getId());
                board.likeBoard(false, likeCount);

            }else {
                boolean checkLike = likeService.checkLike(board.getId(), authorization);
                Long likeCount = likeService.likeCount(board.getId());
                board.likeBoard(checkLike, likeCount);
            }

            Board.Response response = new Board.Response(board);
            responseList.add(response);
        }
        return new Message(true, "게시판을 조회하였습니다.", responseList);
    }

    @Transactional
    public Message GetDetailBoard(Long boardId, String authorization){
        Board board = boardRepository.findAllById(boardId);
        boolean checkLike = likeService.checkLike(board.getId(), authorization);
        Long likeCount = likeService.likeCount(board.getId());
        board.likeBoard(checkLike, likeCount);
        board.viewCount(board.getViewCount()+1);
        Board.Response response = new Board.Response(board);
        return new Message(true, "게시판을 조회하였습니다.", response);
    }

    @Transactional
    public Message GetSaveBoard(Long boardId){
        Board board = boardRepository.findAllById(boardId);
        Board.SaveItemResponse response = new Board.SaveItemResponse(board);
        return new Message(true, "게시판을 조회하였습니다.", response);
    }


    @Transactional
    public Message postBoard(Board.Request request, String authorization, MultipartFile multipartFile) throws IOException {
        Optional<User> user = getUser(authorization);
        GoalItem goalItem = goalItemRepository.findAllById(request.getGoalItemId());
        Board board = new Board(request, goalItem, user);
        if(multipartFile != null) {
            String url = s3Uploader.upload(multipartFile, "static");
            board.changeImage(url);
        }
        boardRepository.save(board);
        return new Message(true, "게시글을 등록하였습니다");
    }

    @Transactional
    public Message putBoard(Board.Update request, Long boardId, String authorization, MultipartFile multipartFile) throws IOException {
        Optional<User> user = getUser(authorization);
        Board board = boardRepository.findAllById(boardId);
        if (user.get().getId().equals(board.getUser().getId())) {
            board.updateBoard(request);
            if(multipartFile != null) {
                String url = s3Uploader.upload(multipartFile, "static");
                board.changeImage(url);
            }
            return new Message(true, "게시글을 수정하였습니다");
        } else {
            return new Message(false, "게시글을 수정에 실패하였습니다");
        }

    }

    @Transactional
    public Message deleteBoard(Long boardId, String authorization) {
        Optional<User> user = getUser(authorization);
        Board board = boardRepository.findAllById(boardId);
        if (user.get().getId().equals(board.getUser().getId())) {
            boardRepository.delete(board);
            return new Message(true, "게시글을 삭제하였습니다");
        } else {
            return new Message(false, "게시글을 삭제에 실패하였습니다");
        }
    }


}
