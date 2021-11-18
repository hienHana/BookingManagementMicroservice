package com.synex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synex.domain.Reviews;
import com.synex.repository.ReviewsRepository;

@Service
public class ReviewsServiceImpl implements ReviewsService {

	@Autowired ReviewsRepository reviewsRepo;

	@Override
	public Reviews save(Reviews review) {
		return reviewsRepo.save(review);
	}

	@Override
	public List<Reviews> findReviewsByBookingId(int bookingId) {	
		return reviewsRepo.findReviewsByBookingId(bookingId);
	}
	
	
	
}
