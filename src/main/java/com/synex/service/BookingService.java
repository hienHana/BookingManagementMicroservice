package com.synex.service;

import java.util.List;
import java.util.Map;

import com.synex.domain.Booking;


public interface BookingService {
	public List<Object[]> findRoomIdsByInOutDate(String in, String out);
	
	public List<Integer> findHotelIdsByInOutDate(String in, String out);
	
	public Booking saveBooking(Booking booking);
	
	public List<Booking> findBookingByUserId(long userId);
	
	public Booking findBookingById(int bookingId);
	
	public Booking cancelBooking(int bookingId);
	
	public Booking saveFeedback(int bookingId, int starRating,String feedbackText);
	
	public Map<Integer, Integer>  calculateAverageStarRatingPerRoom();
	
	public List<Integer> getRoomsByHotelSelectionWithDateSearch(int hotelIdSel,String in);
	
}
