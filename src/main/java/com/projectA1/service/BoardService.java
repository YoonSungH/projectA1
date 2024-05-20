package com.projectA1.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectA1.model.Board;
import com.projectA1.model.User;
import com.projectA1.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class BoardService {
	private final BoardRepository boardRepository;
	
	//추가
	 @Transactional
	    public void insert(Board board, User user) {
	        board.setUser(user);
	        boardRepository.save(board);
	    }
	//전체보기(페이징 + 검색)
	public Page<Board> findAll(String field,String word,Pageable pageable){
		Page<Board>lists = boardRepository.findAll(pageable);
		if(field.equals("title")) {
			lists = boardRepository.findByTitleContaining(word,pageable);
		}else if(field.equals("content")) {
			lists = boardRepository.findByContentContaining(word,pageable);
		}
		return lists;
	}
	//전체보기(페이징)
	public Page<Board> findAll(Pageable pageable){
		return boardRepository.findAll(pageable);
	}
	//전체보기
	public List<Board> list() {
		return boardRepository.findAll();
	}
	//개수(검색)
	public long count(String field,String word) {
		long count = boardRepository.count();
		if(field.equals("title")) {
			count = boardRepository.cntTitleSearch(word);
		}else if(field.equals("content")) {
			count = boardRepository.cntContentSearch(word);
		}
		return count ;
	}
	//개수
	public Long count() {
		return boardRepository.count();
	}
	//상세보기
	@Transactional
	public Board view(Long num) {
		//조회수 증가
		Board board = boardRepository.findById(num).get(); 
		board.setHitcount(board.getHitcount()+1);
		return board;
	}
	//삭제
	@Transactional
	public void delete(Long num) {
		boardRepository.deleteById(num);
	}
	//업데이트
	@Transactional
	public void update(Board board) {
		Board b = boardRepository.findById(board.getNum()).get();
		b.setTitle(board.getTitle());
		b.setContent(board.getContent());
		b.setRegdate(new Date());
		
	}
	public Optional<Board> findbyId(Long id) {
		// TODO Auto-generated method stub
		return boardRepository.findById(id);
	}
	

}
