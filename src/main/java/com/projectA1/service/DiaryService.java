package com.projectA1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projectA1.model.Diary;
import com.projectA1.model.User;
import com.projectA1.repository.DiaryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {
	
	private final DiaryRepository diaryrepository;
	
	public List<Diary> findAll(){
		return diaryrepository.findAll();
	}
	
	public Diary addDiary(Diary diary) {
		return diaryrepository.save(diary);
	}

	public List<Diary> findAllByUserId(Long userId) {
	    return diaryrepository.findAllByUserId(userId);
	}
	
	// 다이어리 전체보기
	@Transactional
	public Diary diaryview(long id) {
		Diary diary = diaryrepository.findById(id).get();
		return diary;
	}
	
	public Diary findById(Long id) {
		 return diaryrepository.findById(id).orElse(null);
	}
	
	 @Transactional
	    public Diary updateDiary(Diary updatedDiary) {
	        // 업데이트된 다이어리 저장
	        return diaryrepository.save(updatedDiary);
	    }
	
	// 다이어리 삭제
	public void deleteDiary(long id) {
	    diaryrepository.deleteById(id);
	}

	public void deleteByUserId(Long id) {
		diaryrepository.deleteAllByUserId(id);
		
	}

}
