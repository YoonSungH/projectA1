package com.projectA1.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.projectA1.model.FitnessCenter;
import com.projectA1.model.Review;
import com.projectA1.model.ReviewData;
import com.projectA1.model.User;
import com.projectA1.service.FitnessCenterService;
import com.projectA1.service.ReviewService;
import com.projectA1.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/review/*")
@RequiredArgsConstructor
@Log4j2
public class ReviewController {

	private final ReviewService reviewService;
	private final FitnessCenterService fitnessCenterService;
	private final UserService userService;
		
	// 댓글 추가
	@PostMapping("add")
	@ResponseBody
	@Transactional
	public String addReview(@RequestBody ReviewData reviewData) {		
		
	        // 사용자 ID와 센터 ID로 User 및 FitnessCenter 객체 조회
	        User user = userService.findById(reviewData.getUserId()).get();
	        FitnessCenter center = fitnessCenterService.findByCenter(reviewData.getCenterId()).get();

	        // Review 객체 생성
	        Review review = new Review();
	        review.setRating(reviewData.getRating());
	        review.setReviewText(reviewData.getReviewText());
	        review.setUser(user);
	        review.setCenter(center);
	        
	        // Review 저장
	        reviewService.addReview(review);
	        return "success";
	}

	// 댓글 삭제
	@GetMapping("/delete/{id}")
	@ResponseBody
	public String deleteReview(@PathVariable Long id) {
		reviewService.deleteReview(id);
		return "success";
	}

	// 댓글 수정폼
	@GetMapping("/edit/{id}")
	public String editReview(@PathVariable Long id, Model model) {
		Review review = reviewService.getReviewById(id);
		model.addAttribute("review", review);
		return "editReview";
	}

	// 댓글 수정
	@PostMapping("/edit")
	@ResponseBody
	public String updateReview(@RequestBody Review review) {
		reviewService.updateReview(review);
		return "success";
	}

	// 댓글 전체보기
	@GetMapping("all")
	public List<Review> getAllReviews() {
		return null;
	    //return reviewService.getAllReviews();
	}

}
