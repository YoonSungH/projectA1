package com.projectA1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.projectA1.config.auth.PrincipalUser;
import com.projectA1.model.Board;
import com.projectA1.model.Diary;
import com.projectA1.model.Owner;
import com.projectA1.model.Person;
import com.projectA1.model.User;
import com.projectA1.service.BoardService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestMapping("/board/")
@RequiredArgsConstructor
@Controller
public class BoardController {
	private final BoardService boardService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("insert")
	public String insert() {
		return "board/insert";
	}
	//글 추가
	@PostMapping("insert")
	public String insert(Board board, @AuthenticationPrincipal PrincipalUser principal) {
	    if (principal != null && principal.getUser() instanceof User) {
	        boardService.insert(board, (User) principal.getUser());
	    } else {
	        // 사용자가 인증되지 않았거나 사용자 정보가 올바르지 않은 경우 처리할 로직 추가
	        // 예를 들어, 로그인 페이지로 리다이렉트하거나 예외를 발생시킬 수 있습니다.
	    }
	    return "redirect:list";
	}
	//전체보기(페이징+검색)
	@GetMapping("list")
	public String list(Model model,
			@PageableDefault(size=5,sort="num",direction=Direction.DESC)Pageable pageable,
			@RequestParam(required=false,defaultValue = "") String field,
			@RequestParam(required=false,defaultValue = "") String word) {
		Page<Board> lists = boardService.findAll(field,word,pageable);
		long count = boardService.count(field,word);
		model.addAttribute("count",count);
		model.addAttribute("boards",lists);
		return "board/list";
	}
	//전체보기(페이징 ==> 페이징 정보+ 데이터 (Board 정보)
//	@GetMapping("list")
//	public String list(Model model ,
//			@PageableDefault(size=5,sort="num",direction=Direction.DESC) Pageable pageable) {
//		Page<Board> lists = boardService.findAll(pageable);
//		model.addAttribute("count",boardService.count());
//		model.addAttribute("boards",lists);
//		return "board/list";
	
	//전체 보기
//	@GetMapping("list")
//	public String list(Model model) {
//		model.addAttribute("count",boardService.count());
//		model.addAttribute("boards",boardService.list());
//		return "board/list";
//	}
	
	//상세보기
	@GetMapping("view/{num}")
	public String view(@AuthenticationPrincipal PrincipalUser principalUser  ,@PathVariable Long num,Model model) {
		//여기서 문제임 이 부분 수정하면 됨
		
		Person user = null;
		if(principalUser.getUser() instanceof User) {
			user = (User) principalUser.getUser();
		}else if(principalUser.getUser() instanceof Owner) {
			user = (Owner) principalUser.getUser();
		}
		Long userId = user.getId();
		model.addAttribute("Id", userId);
		model.addAttribute("board",boardService.view(num));
		return "board/view";
	}
	
	
	
	//삭제
	@GetMapping("delete/{num}")
	@Transactional
	public String delete(@PathVariable Long num) {
		boardService.delete(num);
		return "/board/list";
	}
	//수정폼
	@GetMapping("update/{num}")
	public String update(@PathVariable Long num,Model model) {
		model.addAttribute("board",boardService.view(num));
		return "board/update";
	}
	
	@PostMapping("update/{num}")
	public String update(@PathVariable Long num, @ModelAttribute Board updatedBoard) {
	    Board board = boardService.findbyId(num).get();
	    board.setTitle(updatedBoard.getTitle());
	    board.setContent(updatedBoard.getContent());
	    boardService.update(board);
	    return "redirect:../../board/list";
	}

	

}
