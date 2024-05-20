package com.projectA1.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.projectA1.config.auth.PrincipalUser;
import com.projectA1.model.FitnessCenter;
import com.projectA1.model.Owner;
import com.projectA1.model.Reservation;
import com.projectA1.model.User;
import com.projectA1.model.VisitCounting;
import com.projectA1.repository.VisitCountingRepository;
import com.projectA1.service.ReservationService;
import com.projectA1.service.VisitCountingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reservation/*")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;
	private final VisitCountingService visitCountingService; // 방문횟수 저장

	// 예약 등록
	@PostMapping("create")
	@ResponseBody
	public String createReservation(@AuthenticationPrincipal PrincipalUser principalUser, @RequestParam Long centerId) {
		// 센터 아이디를 이용하여 예약 객체 생성
		Reservation reservation = new Reservation();

		// 유저id 객체 생성
		User user = (User) principalUser.getUser();
		// 센터 아이디 설정
		FitnessCenter center = new FitnessCenter();
		// 시간 설정
		Date currentTime = new Date();
		center.setId(centerId);
		reservation.setUser(user);
		reservation.setCenter(center);
		reservation.setReservationTime(currentTime);

		// 예약 서비스를 통해 예약 생성
		reservationService.create(reservation);

		return "success";
	}
	
	

	// 예약지점 상세보기
	@GetMapping("view/{reservationId}")
	public String view(@PathVariable Long reservationId, Model model) {
		model.addAttribute("reservation", reservationService.findReservation(reservationId));
		return "/reservation/view";
	}

	// 예약 취소
	@GetMapping("delete/{reservationId}")
	public String delete(@PathVariable Long reservationId) {
		reservationService.delete(reservationId);
		return "redirect:/user/mypage";
	}

	// 예약 수정폼(모달로 처리)
//	@GetMapping("update/{reservationId}")
//	public String update(@PathVariable Long reservationId, Model model) {
//		Optional<Reservation> reservation = reservationService.findReservation(reservationId);	
//		model.addAttribute("reservation",reservation.get());
//		return "/reservation/updateForm";
//	}
//	
//	//예약수정
//	@PutMapping("update/{reservationId}")
//	@ResponseBody
//	public String update(@PathVariable Long reservationId, @RequestParam Date newReservationTime) {
//		Optional<Reservation> reservation = reservationService.findReservation(reservationId);
//		
//		reservation.get().setReservationTime(newReservationTime);
//		reservationService.create(reservation.get());
//		return "success";
//	}

	// 예약 사용함(임시로 만들어둔 버튼)
	@GetMapping("used/{reservationId}")
	public String used(@PathVariable Long reservationId) {
		Optional<Reservation> reservation = reservationService.findReservation(reservationId);
		User user = reservation.get().getUser();
		FitnessCenter center = reservation.get().getCenter();

		// 유저와 센터 기록 존재 여부 파악위함
		VisitCounting visitCounting  = new VisitCounting(user, center);

//		// 예약한 기록이 있다면 카운트 +1 만 추가해주기
//		if (visitCounting != null) {
//			count = visitCounting.getVisitCount();
//			visitCounting.setVisitCount(count + 1);
//		} else
//		// 아니라면 새롭게 만들어서 추가해주기
//		{
//			visitCounting = new VisitCounting(user, center, count + 1);
//		}

		// 방문기록
		visitCountingService.visit(visitCounting);
		// 예약기록 삭제
		reservationService.delete(reservationId);
		return "redirect:/user/mypage";
	}

}
