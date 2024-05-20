package com.projectA1.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectA1.config.auth.PrincipalUser;
import com.projectA1.model.Diary;
import com.projectA1.model.User;
import com.projectA1.service.DiaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/diary/*")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;

	// 전체보기
	@GetMapping("diaryList")
	public String list(@AuthenticationPrincipal PrincipalUser principalUser, Model model) {
		User user  = (User) principalUser.getUser();
		// 로그인된 사용자의 정보를 가져옵니다.
		Long userId = user.getId();
		List<Diary> list = diaryService.findAllByUserId(userId);
		model.addAttribute("diarys", list);
		return "/diary/diaryList";		
	}

	@GetMapping("diaryNew")
	public String addDiary() {
		return "/diary/diaryNew";
	}

	// 기록 추가
	@PostMapping("addDiary")
	public String addDiary(Diary diary, @AuthenticationPrincipal PrincipalUser principal) {
		User user = (User) principal.getUser();
		diary.setUser(user);
		diaryService.addDiary(diary);
		return "redirect:/diary/diaryList";
	}
	
	// 상세보기
	@GetMapping("diaryView/{id}")
	public String diaryView(Model model, @PathVariable long id) {
		model.addAttribute("diary", diaryService.diaryview(id));
		return "diary/diaryView";
	}
	
	// 다이어리 수정 화면으로 이동
    @GetMapping("/diaryEdit/{id}")
    public String editDiary(@PathVariable Long id, Model model) {
        Diary diary = diaryService.findById(id);
        model.addAttribute("diary", diary);
        return "/diary/diaryEdit";
    }
    
    // 다이어리 수정 처리
    @PostMapping("/updateDiary/{id}")
    public String updateDiary(@PathVariable Long id, @ModelAttribute Diary updatedDiary) {
        if (id == null) {
            return "redirect:/error";
        }
        // 기존 다이어리 가져오기
        Diary existingDiary = diaryService.findById(id);
        if (existingDiary == null) {
            return "redirect:/error";
        }
        // 기존 다이어리 정보 업데이트
        existingDiary.setTitle(updatedDiary.getTitle());
        existingDiary.setDate(updatedDiary.getDate());
        existingDiary.setContent(updatedDiary.getContent());

        // 업데이트 된 다이어리 저장
        diaryService.updateDiary(existingDiary);

        return "redirect:/diary/diaryList";
    }
    
	// 다이어리 삭제
	@GetMapping("/diaryDelete/{id}")
	public String deleteDiary(@PathVariable long id) {
	    diaryService.deleteDiary(id);
	    return "redirect:/diary/diaryList";
	}

}
