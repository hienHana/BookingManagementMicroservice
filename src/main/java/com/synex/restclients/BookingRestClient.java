package com.synex.restclients;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.synex.domain.Booking;
import com.synex.domain.BookingDetails;

@Component
public class BookingRestClient {
	@Autowired RestTemplate restTemplate;
	
	public List<BookingDetails>  getBookingDetailInName(List<Booking> bookings){
		
		List<BookingDetails> bookedList = new ArrayList<>();
		if(!bookings.isEmpty()) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			for(Booking b:bookings) {
				BookingDetails bookingDetails = new BookingDetails();
				
				int hotelId = b.getHotelId();
				final String hotelNameUri = "http://localhost:8383/getHotelNameByHotelId/{hotelId}";
				String hotelName = restTemplate.getForObject(hotelNameUri, String.class, hotelId);
				bookingDetails.setHotelName(hotelName);
				
				int roomId = b.getRoomId();
				final String roomTypeUri = "http://localhost:8383/getRoomTypeByRoomId/{roomId}";
				String roomType = restTemplate.getForObject(roomTypeUri, String.class, roomId);
				bookingDetails.setRoomType(roomType);
				
				bookingDetails.setCheckIn(format.format(b.getCheckIn()));
				bookingDetails.setCheckOut(format.format(b.getCheckOut()));
				
				System.out.println("hotelName: "+hotelName+" roomType: "+roomType+" checkin: "+format.format(b.getCheckIn()));
				
				bookingDetails.setBookingId(b.getBookingId());
				bookingDetails.setNoGuests(b.getNoOfGuests());
				bookingDetails.setNoRooms(b.getNoOfRooms());
				bookingDetails.setTotalPrice(b.getTotalPrice());
				bookedList.add(bookingDetails);
			}
		}
		return bookedList;
	} 
	
	
	public void updateHotelRating(Map<Integer, Integer> avgStarsPerRoom) {
		final String uri = "http://localhost:8383/updateHotelRating";
		restTemplate.postForObject(uri, avgStarsPerRoom, String.class);
	}
}
