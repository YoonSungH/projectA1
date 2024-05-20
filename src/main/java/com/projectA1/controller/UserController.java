package com.projectA1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.projectA1.config.auth.PrincipalUser;
import com.projectA1.model.FitnessCenter;
import com.projectA1.model.Reservation;
import com.projectA1.model.User;
import com.projectA1.service.DiaryService;
import com.projectA1.service.FitnessCenterService;
import com.projectA1.service.OwnerService;
import com.projectA1.service.ReservationService;
import com.projectA1.service.UserService;
import com.projectA1.service.VisitCountingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/user")	// 주소를 요청했을 때 모든 것에 반응
@RequiredArgsConstructor
public class UserController {

	// 사용자 추가
	// 사용자 마이페이지 => 정보수정, 회원탈퇴

	private final UserService userService;
	private final OwnerService ownerService;
	private final VisitCountingService visitCountingService;
	private final ReservationService reservationService;
	private final FitnessCenterService fitnessCenterService;
	private final DiaryService diaryService;
	
	// 사용자 추가 => 추가 후, 로그인 페이지
	@PostMapping("join")
	@ResponseBody
	public String join(@RequestBody User user) {
	    // 사용자 이메일 중복 확인
	    if (userService.existsByEmail(user.getEmail()) || ownerService.existsByEmail(user.getEmail())) {
	    	 return "fail";
	    }else {
		    // 사용자 역할 설정
		    List<String> roles = new ArrayList<>();
		    roles.add("ROLE_USER");
		    user.setRole(roles);
		    
		    // 사용자 추가
		    userService.join(user);
	    }	    
        return "success";
	}

	// 사용자 마이페이지(상세보기) => 예약자 리스트도 표시
	// 이거 기준으로 짜면됩니다.
	@GetMapping("mypage")
	public String viewMyPage(@AuthenticationPrincipal PrincipalUser principalUser, Model model) {
		// 로그인된 사용자의 정보를 가져옵니다.
		User user = (User) principalUser.getUser();
		List<Reservation> reservations = reservationService.findByUserId(user.getId());
		// 센터 방문횟수를 가져와 화면에 표시(userid, centerid)
		long visitCount = visitCountingService.visitCounting(user.getId());

		// 사용자의 상위 3개 센터 방문 정보를 가져옵니다.
		// centerId를 가져오기에 해당 센터명을 불러와서 사용해야 합니다.
		List<Object[]> top3VisitedCenters = visitCountingService.findTop3VisitedCenters(user.getId());
		for (int i = 0; i < top3VisitedCenters.size(); i++) {
		    Long centerId = (Long) top3VisitedCenters.get(i)[0];
		    top3VisitedCenters.get(i)[0] = fitnessCenterService.findByCenterName(centerId);
		}
		
		//등수 이모티콘
		String[] emoticon = {"🥇","🥈","🥉"};
		
		model.addAttribute("emoticon",emoticon);
		model.addAttribute("top3VisitedCenters", top3VisitedCenters);
		model.addAttribute("visitCount", visitCount);
		model.addAttribute("user", user);
		model.addAttribute("reservations", reservations);
		return "/user/mypage";

	}

	// 사용자 정보수정폼
	@GetMapping("updateForm")
	public String updateForm(@AuthenticationPrincipal PrincipalUser principalUser, Model model) {
		// 로그인된 사용자의 정보를 가져옵니다.
		User user = (User) principalUser.getUser();
		model.addAttribute("user", user);
		return "/user/updateForm";
	}

	// 사용자 정보수정 => ajax 비동기 처리
	@PostMapping("update")
	// updateUser -> ajax 받아온 데이터 user
	public String update(@AuthenticationPrincipal PrincipalUser principalUser, @RequestBody User updateUser) {
		User currentUser = (User) principalUser.getUser();
		userService.update(currentUser, updateUser);
		return "success";
	}

	// 사용자 회원탈퇴
	@Transactional
	@DeleteMapping("delete")
	public String delete(@AuthenticationPrincipal PrincipalUser principalUser, HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) principalUser.getUser();
		//다이어리 삭제하려고함
		diaryService.deleteByUserId(user.getId());
		
		userService.delete(user.getEmail());

		// 세션 무효화
		invalidateSession(request);

		return "success";
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	// 세션 무효화
	private void invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
}