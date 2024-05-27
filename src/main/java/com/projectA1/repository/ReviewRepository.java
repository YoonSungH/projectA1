package com.projectA1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectA1.model.FitnessCenter;
import com.projectA1.model.Review;
import com.projectA1.model.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>{
	
	List<Review> findAllByUserId(Long userId);
	
	List<Review> findAllByCenterId(Long centerId);
	//리뷰 전체가져오기
	List<Review> findByCenterId(Long id);
	
	Page<Review> findByCenterId(Long centerId, Pageable pageable);

	//해당유저 테이블 정보 삭제
	void deleteAllById(Long id);
	
    // 사용자에 대한 리뷰 삭제
    void deleteByUser(User user);

    //해당 센터 리뷰 전체 삭제
	void deleteAllByCenter(FitnessCenter fitnessCenter);
	
	
}
