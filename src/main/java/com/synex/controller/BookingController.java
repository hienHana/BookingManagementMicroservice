package com.synex.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synex.domain.Booking;
import com.synex.domain.BookingDetails;
import com.synex.domain.Feedback;
import com.synex.domain.Reviews;
import com.synex.domain.SearchDetails;
import com.synex.restclients.BookingRestClient;
import com.synex.service.BookingService;
import com.synex.service.ReviewsService;

@CrossOrigin
@RestController
public class BookingController {
	@Autowired BookingService bookingService;
	@Autowired BookingRestClient bookingRestClient;
	@Autowired ReviewsService reviewsService;
	
	@RequestMapping(value = "/getHotelIdsAvailByInOutkDate", method = RequestMethod.POST)
	public ResponseEntity<List<Integer>>  getHotelIdsAvailByInOutkDate(@RequestBody SearchDetails searchDetails ){
		System.out.println("Inside Booking Controller "+searchDetails.getCheckIn() + ", "+searchDetails.getCheckOut());
		List<Integer> roomIdsNotAvail = bookingService.findHotelIdsByInOutDate(searchDetails.getCheckIn(),searchDetails.getCheckOut());
		
		return new ResponseEntity<List<Integer>>(roomIdsNotAvail, HttpStatus.OK);
	} 
	
	@RequestMapping(value = "/getRoomIdsAvailByInOutkDate", method = RequestMethod.POST)
	public ResponseEntity<List<Object[]>>  getRoomIdsAvailByInOutkDate(@RequestBody SearchDetails searchDetails ){
		System.out.println("Inside Booking Controller "+searchDetails.getCheckIn() + ", "+searchDetails.getCheckOut());
		List<Object[]> roomIdsNotAvail = bookingService.findRoomIdsByInOutDate(searchDetails.getCheckIn(),searchDetails.getCheckOut());
		
		return new ResponseEntity<List<Object[]>>(roomIdsNotAvail, HttpStatus.OK);
	} 
	
	//get rooms from hotel selection and data filter
	@RequestMapping(value = "/getRoomsIdByHotelIdWithDateSearch/{hotelIdSel}/{checkInDate}", method = RequestMethod.GET)
	public ResponseEntity<List<Integer>> getRoomsIdByHotelIdWithDateSearch(@PathVariable int hotelIdSel, @PathVariable String checkInDate){
		List<Integer> roomIds = bookingService.getRoomsByHotelSelectionWithDateSearch(hotelIdSel,checkInDate);
		return new ResponseEntity<List<Integer>>(roomIds, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/saveBooking", method = RequestMethod.POST)
	public ResponseEntity<Booking>  saveBooking (@RequestBody Booking bookingDetail ){
		System.out.println("Inside Booking-> BookingDetail: "+ bookingDetail );
		Booking booking = bookingService.saveBooking(bookingDetail);
		return new ResponseEntity<Booking>(booking, HttpStatus.OK);
	} 
	
	
	@RequestMapping(value = "/getBookingsByUserId/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<BookingDetails>>  getBooking (@PathVariable long userId){
		List<Booking> bookings = bookingService.findBookingByUserId(userId);
		System.out.println("Inside Booking-> booking_bookingDetail: "+ bookings);
		List<BookingDetails> bookingDetailList = new ArrayList<>();
		if(!bookings.isEmpty()) {
			bookingDetailList = bookingRestClient.getBookingDetailInName(bookings);
		}
		return new ResponseEntity<List<BookingDetails> >(bookingDetailList, HttpStatus.OK);
	} 
	
	
	@RequestMapping(value = "/cancelBooking/{bookingId}", method = RequestMethod.GET)
	public ResponseEntity<Booking>  cancelBooking (@PathVariable int bookingId ){
		System.out.println("Inside Booking-> BookingId: "+ bookingId );
		Booking booking = bookingService.cancelBooking(bookingId);
		System.out.println("Inside Booking-> Booking: "+ booking );
		return new ResponseEntity<Booking>(booking, HttpStatus.OK);
		
	} 
	
	@RequestMapping(value = "/saveFeedback", method = RequestMethod.POST)
	public ResponseEntity<Booking>  saveBooking (@RequestBody Feedback feedback ){
		System.out.println("Inside Booking-> feedbackObj: "+ feedback );
		
		//convert feedback object to components
		int bookingId = feedback.getBookingId();
		int starRating = feedback.getStarRating();
		String feedbackText = feedback.getFeedback();	
		
		System.out.println("bookingId: "+bookingId+",starRating: "+starRating+",feedbackText: "+feedbackText);

		//save feedback to Reviews table and reference to booking table
		Booking booking = bookingService.saveFeedback(bookingId, starRating,feedbackText);
		
		//change the star rating by people of hotels
		Map<Integer, Integer> avgStarsPerRoom =  bookingService.calculateAverageStarRatingPerRoom();
		bookingRestClient.updateHotelRating(avgStarsPerRoom);
		
		return new ResponseEntity<Booking>(booking, HttpStatus.OK);
	} 
	
	
	@RequestMapping(value = "/getReviewByUserId/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Reviews>> getReviewByUserId(@PathVariable long userId ){
		System.out.println("Inside Booking-> userId: "+ userId );
		List<Booking> bookings = bookingService.findBookingByUserId(userId);
		System.out.println("Inside Booking-> Booking: "+ bookings );
		
		List<Reviews> reviews = new ArrayList<>();
		for(Booking b:bookings) {
			reviews = reviewsService.findReviewsByBookingId(b.getBookingId());
		}		 
		return new ResponseEntity<List<Reviews>>(reviews, HttpStatus.OK);
		
	} 
}
