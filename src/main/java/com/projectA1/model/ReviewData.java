package com.projectA1.model;

public class ReviewData {
    private Long userId;
    private Long centerId;
    private int rating;
    private String reviewText;

    // 생성자, 게터, 세터 등 필요한 메서드를 추가할 수 있음

    // 생성자
    public ReviewData() {}

    // 게터와 세터 메서드
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCenterId() {
        return centerId;
    }

    public void setCenterId(Long centerId) {
        this.centerId = centerId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
