package com.synex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.synex.domain.Reviews;


public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {
	
	@Query(value="select * from reviews r, booking_reviews br where r.feedbackId = br.reviews_feedbackId "
			+ " and br.booking_bookingId=?1", nativeQuery=true)
	public List<Reviews> findReviewsByBookingId(int bookingId);
	
}
