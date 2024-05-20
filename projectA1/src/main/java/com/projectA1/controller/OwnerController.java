package com.projectA1.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.projectA1.model.Owner;
import com.projectA1.model.Reservation;
import com.projectA1.service.FitnessCenterService;
import com.projectA1.service.OwnerService;
import com.projectA1.service.ReservationService;
import com.projectA1.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/owner/*")
@RequiredArgsConstructor
public class OwnerController {

	// 오너 추가
	// 오너 마이페이지 => 정보수정, 회원탈퇴

	private final UserService userService;
	private final OwnerService ownerService;
	private final FitnessCenterService fitnessCenterService;
	private final ReservationService reservationService;

	// 오너 마이페이지
	@GetMapping("ownerpage")
	public String ownerPage(@AuthenticationPrincipal PrincipalUser principalUser, Model model) {
		// 사용자 정보를 통해 해당 사용자가 관리하는 센터의 이름을 조회하여 모델에 추가합니다.
		Owner owner = (Owner) principalUser.getUser();

		if (owner.getFitnessCenter() != null) {
			Long centerId = owner.getFitnessCenter().getId();
			List<Reservation> reservations = reservationService.findByCenterId(centerId);
			String centerName = fitnessCenterService.findByCenterName(owner.getFitnessCenter().getId());
			model.addAttribute("centerName", centerName);
			model.addAttribute("reserve", reservations);
		} else {
			model.addAttribute("centerName", "notExist");
		}

		return "/owner/ownerpage";
	}

	// 오너 추가폼 변경완료
	@GetMapping("join")
	public String join() {
		return "/owner/join";
	}

	// 오너 정보추가 => 추가 후, 로그인 페이지
	@PostMapping("join")
	@ResponseBody
	public String join(@RequestBody Owner owner) {

		// 사용자 이메일 중복 확인
		if (userService.existsByEmail(owner.getEmail()) || ownerService.existsByEmail(owner.getEmail())) {
			return "fail"; // 중복된 이메일이 존재하는 경우 실패 반환
		} else {
			List<String> roles = new ArrayList<>();
			roles.add("ROLE_OWNER");
			owner.setRole(roles);
			ownerService.join(owner);
		}
		return "success"; // 페이지 수정 필요
	}

	// 오너 마이페이지(상세보기)
	@GetMapping("view/{id}")
	public String view(@AuthenticationPrincipal PrincipalUser principalUser, Model model) {
		Owner owner = (Owner) principalUser.getUser();
		model.addAttribute("owner", ownerService.view(owner.getId()));
		return "/owner/view";
	}

	// 오너 정보수정폼
	@GetMapping("updateForm")
	public String update(@AuthenticationPrincipal PrincipalUser principalUser, Model model) {
		Owner owner = (Owner) principalUser.getUser();
		model.addAttribute("owner", ownerService.view(owner.getId()));
		return "/owner/updateForm";
	}

	// 오너 정보수정
	@PostMapping("update")
	@ResponseBody
	public String update(@AuthenticationPrincipal PrincipalUser principalUser, @RequestBody Owner owner) {
		Owner currentOwner = (Owner) principalUser.getUser();
		ownerService.update(currentOwner, owner);
		return "success";
	}

	// 오너 회원탈퇴
	@DeleteMapping("delete")
	@ResponseBody
	@Transactional
	public String delete(@AuthenticationPrincipal PrincipalUser principalUser, HttpServletRequest request,
			HttpServletResponse response) {
		Owner owner = (Owner) principalUser.getUser();
		ownerService.delete(owner.getId());
		// 세션 무효화
		invalidateSession(request);
		return "success";
	}

	// 세션 무효화
	private void invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
}
