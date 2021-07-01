package com.cos.blog.service;

import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해준다.
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public void commit(Board board, User user){ //title, content
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> list(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board showDetail(int id) {
        return boardRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public void delete(int id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Board requestBoard) {
        Board board = boardRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
        }); //영속화 완료
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        // 해당 함수 종료시(Service가 종료될 때) 트랜잭션이 종료됩니다. 이때 더티체킹 - 자동 업데이트가 됨. db flush
    }

    @Transactional
    public void replyCommit(User user, int boardId, Reply requestReply) {

        Board board = boardRepository.findById(boardId).orElseThrow(()->{
            return new IllegalArgumentException("댓글 쓰기 실패 : 게시글 id를 찾을 수 없습니다.");
        });

        requestReply.setUser(user);
        requestReply.setBoard(board);

        replyRepository.save(requestReply);
    }

    @Transactional
    public void replyDelete(int replyId) {
        replyRepository.deleteById(replyId);
    }
}
