package com.projectA1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projectA1.model.FitnessCenter;
import com.projectA1.model.Review;
import com.projectA1.model.ReviewData;
import com.projectA1.model.User;
import com.projectA1.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	
    private final ReviewRepository reviewRepository;
	private final FitnessCenterService fitnessCenterService;
		@Transactional
	    public void addReview(Review review) {
	        reviewRepository.save(review);
	    }

	
	    public void deleteReview(Long id) {
	        reviewRepository.deleteById(id);
	    }

	
	    public Review getReviewById(Long id) {
	        return reviewRepository.findById(id).orElse(null);
	    }

	
	    public void updateReview(Review review) {
	        reviewRepository.save(review);
	    }


	    public List<Review> getAllReviews(Long centerId) {
	        return reviewRepository.findAllByCenterId(centerId);
	    }


	    public List<Review> findByCenterId(Long id) {
	        return reviewRepository.findAllByCenterId(id);
	    }
	    
	    
	    public Page<Review> findByCenterId(Long centerId, Pageable pageable) {
	        return reviewRepository.findByCenterId(centerId, pageable);
	    }

	    // 사용자 ID에 해당하는 리뷰 삭제
	    @Transactional
	    public void deleteByUserId(User user) {
	        reviewRepository.deleteByUser(user);
	    }

	    //센터 리뷰 전체 삭제 ==> 센터삭제를 위함
		public void deleteByCenterReview(FitnessCenter fitnessCenter) {
			reviewRepository.deleteAllByCenter(fitnessCenter);
		}
}
