package com.kye.iStudyManageApp.sample.service;

import com.kye.iStudyManageApp.sample.dto.ReplySaveRequestDto;
import com.kye.iStudyManageApp.sample.model.Board;
import com.kye.iStudyManageApp.sample.model.Reply;
import com.kye.iStudyManageApp.sample.model.User;
import com.kye.iStudyManageApp.sample.repository.BoardRepository;
import com.kye.iStudyManageApp.sample.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록해줌, IoC해줌
@Service
public class BoardService {


    @Autowired  // DI로 주입됨
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    /***************
     * 글쓰기 저장
     * title, content는 클라이언트에서 받아서 저장
     * user정보와 조회수 (count)는 여기서 만들어서 넣는다.
     ****************/
    @Transactional // 하나의 트랜잭션으로 묶는다.
    public void 글저장(Board board, User user) {
        board.setCount(0);  // default로 0을 셋팅
        board.setUser(user);  // user정보를 셋팅
        boardRepository.save(board);
    }

    /***************
     * 글목록 읽기
     * 페이징 처리 없이 읽기
     ****************/
//    public List<Board> 글목록(){
//        return boardRepository.findAll();
//    }

    /***************
     * 글목록 읽기
     * 페이징 처리 해서 읽기
     ****************/
    @Transactional(readOnly = true)
    public Page<Board> 글목록_페이지(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    /***************
     * 글상세 보기
     ****************/
    @Transactional(readOnly = true)
    public Board 글상세보기(int id){
        return boardRepository.findById(id).orElseThrow(()->{
            // board는 reply정보를 같이 가지고 있다.
            return new IllegalArgumentException("글상세보기 실패 : 아이디를 찾을 수 없습니다.");
        });
    }

    /***************
     * 글 수정
     ****************/
    @Transactional
    public void 글수정하기(Board reqBoard){
        // 수정하려면 먼저 영속화를 시켜야 한다.(원래 저장되어 있던 보드내용을 영속화)
        Board board = boardRepository.findById(reqBoard.getId()).orElseThrow(()->{
            return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
        });

        // 클라이언트에서 올라온 수정내용을 셋팅 한다.
        board.setTitle(reqBoard.getTitle());
        board.setContent(reqBoard.getContent());

        // 본 함수 종료 시 (서비스가 종료될때) 트랜잭션이 종료되면 이때 더티체킹이 일어나서 자동 update가 된다(db flush)
    }

    /***************
     * 글 삭제
     ****************/
    @Transactional
    public void 글삭제(int id){

        boardRepository.deleteById(id);
    }

    /***************
     * 댓글 쓰기 저장
     * DTO 미사용
     ****************/
    @Transactional
    public void 댓글저장(User user, int boardId, Reply requestReply){

        // 클라이언트에서는 content만 올라오기 때문에 여기서 reply에 넣을 데이터를 완성한다.

        Board board = boardRepository.findById(boardId).orElseThrow(()->{
            return new IllegalArgumentException("댓글쓰기 실패 : 게시글 id 를 찾을 수 없습니다.");
        });

        requestReply.setUser(user);
        requestReply.setBoard(board);

        replyRepository.save(requestReply);

        // 본 함수 종료 시 (서비스가 종료될때) 트랜잭션이 종료되면 이때 더티체킹이 일어나서 자동 update가 된다(db flush)

    }

    /***************
     * 댓글 쓰기 저장
     * DTO 사용
     ****************/
    @Transactional
    public void 댓글쓰기DTO(ReplySaveRequestDto replySaveRequestDto) {

        // 개별로 쪼개서 넣어야 한다.
        // 그래야 쿼리에 순서대로 들어감 그래서 객체를 넣을 수 없음
        int result = replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
    }

    /***************
     * 댓글 삭제
     ****************/
    @Transactional
    public void 댓글삭제(int replyId){
        replyRepository.deleteById(replyId);
    }


}
