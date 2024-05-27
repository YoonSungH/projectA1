package com.projectA1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.projectA1.model.FitnessCenter;
import com.projectA1.model.Reservation;
import com.projectA1.model.User;
import com.projectA1.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	
	
	//예약 생성
	public void create(Reservation reservation) {
		reservationRepository.save(reservation);
	}
	
	//예약자 전체보기
	public List<Reservation>viewAll(){
		return reservationRepository.findAll();
	}
	//예약 수정
//	public void update(Reservation reservation) {
//		reservationRepository.
//	}
	
	//예약 삭제
	public void delete(Long reservationId) {
		reservationRepository.deleteById(reservationId);
	}
	
	//자정이 되면 전날 예약 삭제
	// 매일 자정에 실행되는 메서드
    @Scheduled(cron = "0 0 0 * * *")
	//@Scheduled(fixedDelay = 120000)
    public void deletePreviousDayReservations() {
        // 자정 전의 시간을 구합니다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);

        // 전날 자정 시간을 구합니다.
        LocalDateTime previousMidnight = midnight.minusDays(1);

        // 전날 자정 이전의 예약들을 가져옵니다.
        List<Reservation> previousDayReservations = reservationRepository.findByReservationTimeBefore(previousMidnight);

        // 가져온 예약들을 삭제합니다.
        reservationRepository.deleteAll(previousDayReservations);
    }
	
	//매일 자정에 실행되는 메서드
	//  @Scheduled(cron = "0 0 0 * * *")
	// 현재 시간에서 1분 뒤에 실행되는 메서드
//	@Scheduled(fixedDelay = 120000) // 2분 = 120,000 밀리초
//	public void deletePreviousDayReservations() {
//	    // 현재 시간을 구합니다.
//	    LocalDateTime now = LocalDateTime.now();
//
//	    // 1분 뒤의 시간을 계산합니다.
//	    LocalDateTime oneMinuteLater = now.plusMinutes(1);
//
//	    // 예약들을 가져옵니다.
//	    List<Reservation> reservations = reservationRepository.findByReservationTimeBefore(oneMinuteLater);
//
//	    // 가져온 예약들을 삭제합니다.
//	    reservationRepository.deleteAll(reservations);
//	}


    //유저의 예약확인
	public List<Reservation> findByUserId(Long id) {
		// TODO Auto-generated method stub
		return reservationRepository.findAllByUserId(id);
	}
	
	
	//오너의 예약자 확인
	public List<Reservation> findByCenterId(Long centerId) {
        return reservationRepository.findByCenterId(centerId);
    }

	
	//예약정보 찾기
	public Optional<Reservation> findReservation(Long reservationId) {
		// TODO Auto-generated method stub
		return reservationRepository.findById(reservationId);
	}

	public void deleteByUserId(User user) {
		reservationRepository.deleteByUser(user);
		
	}

	//센터 예약 삭제
	public void deleteAllByCenter(FitnessCenter fitnessCenter) {
		reservationRepository.deleteByCenter(fitnessCenter);
	}

}
