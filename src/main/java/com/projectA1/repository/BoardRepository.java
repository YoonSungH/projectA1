package com.projectA1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projectA1.model.Board;

@Repository
public interface BoardRepository  
extends JpaRepository<Board, Long>{
public Page<Board> findByTitleContaining(String word, Pageable pageable);
public Page<Board> findByContentContaining(String word, Pageable pageable);

@Query(value="select count(*) from tbl_board4 where title like CONCAT('%' , :word, '%' )", nativeQuery = true)
public long cntTitleSearch(@Param("word") String word);

@Query(value="select count(*) from tbl_board4 where content like CONCAT('%' , :word, '%' )", nativeQuery = true)
public long cntContentSearch(@Param("word") String   word);

}
