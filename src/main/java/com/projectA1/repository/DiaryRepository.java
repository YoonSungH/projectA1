package com.projectA1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectA1.model.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

	List<Diary> findAllByUserId(Long userId);

	void deleteAllByUserId(Long id);

}
