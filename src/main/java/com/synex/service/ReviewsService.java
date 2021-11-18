package com.synex.service;

import java.util.List;

import com.synex.domain.Reviews;

public interface ReviewsService {
	public Reviews save(Reviews review);
	
	public List<Reviews> findReviewsByBookingId(int bookingId);
}
