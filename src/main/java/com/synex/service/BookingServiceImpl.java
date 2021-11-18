package com.synex.service;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synex.domain.Booking;
import com.synex.domain.Reviews;
import com.synex.repository.BookingRepository;
import com.synex.repository.ReviewsRepository;

@Service
public class BookingServiceImpl implements BookingService {
	@Autowired BookingRepository bookingRepo;
	@Autowired ReviewsRepository reviewsRepo;
	
	@Override
	public List<Object[]> findRoomIdsByInOutDate(String in, String out) {
		if(in != null && !in.isEmpty())
			return bookingRepo.findRoomIdsByInOutDate(in,out);
		else 
			return new ArrayList<Object[]>();
	}

	@Override
	public List<Integer> findHotelIdsByInOutDate(String in, String out) {
		if(in != null && !in.isEmpty())
			return bookingRepo.findHotelIdsByInOutDate(in,out);
		else 
			return new ArrayList<Integer>();
	}

//	@Override
//	public Booking saveBookingDetailsToBooking(int userId, String checkIn, String checkOut,
//			int noGuests,int noRooms,int hotelId, int roomId,double totalPrice,String status, double discount)  {
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Date f_date_in = null;
//		Date f_date_out = null;
//		java.sql.Timestamp sqlTimestamp1= null;
//		java.sql.Timestamp sqlTimestamp2= null;
//		try {
//			System.out.println("checkIn: "+checkIn+"checkout: "+checkOut);
//			f_date_in = format.parse(checkIn);
//			f_date_out = format.parse(checkOut);
//			sqlTimestamp1 = new java.sql.Timestamp( f_date_in.getTime());
//			sqlTimestamp2 = new java.sql.Timestamp( f_date_out.getTime());
//			
//			System.out.println("f_date_in: "+f_date_in+"f_date_out: "+f_date_out);
//			System.out.println("sqlTimestamp1: "+sqlTimestamp1+"sqlTimestamp2: "+sqlTimestamp2);
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("Outside try catch: sqlTimestamp1: "+sqlTimestamp1+" sqlTimestamp2: "+sqlTimestamp2);
//		return bookingRepo.saveBookingDetailsToBooking(userId, sqlTimestamp1, sqlTimestamp2, noGuests, 
//				noRooms, hotelId, roomId, totalPrice,status, discount);
//	}

	@Override
	public List<Booking> findBookingByUserId(long userId) {	
		bookingRepo.updateStatusByUserId(userId);
		return bookingRepo.findBookingByUserId(userId);
	}

	@Override
	public Booking findBookingById(int bookingId) {
		return bookingRepo.findById(bookingId).get();
	}

	@Override
	public Booking saveBooking(Booking booking) {
		return bookingRepo.save(booking);
	}

	@Override
	public Booking cancelBooking(int bookingId) {
		Booking booking = bookingRepo.findById(bookingId).get();
		if(booking != null) {
			booking.setStatus("cancel");
			bookingRepo.save(booking);
		}
		return booking;
	}

	@Override
	public Booking saveFeedback(int bookingId, int starRating, String feedbackText) {
		Reviews review = new Reviews();
		review.setStarRating(starRating);
		review.setFeedback(feedbackText);
		Reviews review1 = reviewsRepo.save(review);
		Booking booking =  bookingRepo.findById(bookingId).get();
		Set<Reviews> set = booking.getReviews();
		set.add(review);
		booking.setReviews(set);
		bookingRepo.save(booking);
		return booking;
	}

	@Override
	public Map<Integer, Integer> calculateAverageStarRatingPerRoom() {
		List<Object[]> objs = bookingRepo.calculateAverageStarRatingHelper();//objs should be sorted by roomId due to order by in query
		for(Object[] o:objs) System.out.println(Arrays.toString(o));
		
		Map<Integer, Integer> avgStarsPerRoom = new HashMap<>(); //key:roomId, value:starAvg per roomId
		
		int count = 1;
		int roomId = ((Number)objs.get(0)[1]).intValue();
		int rating = ((Number)objs.get(0)[0]).intValue();
		
		for(Object[] obj:objs) {
			if(roomId == ((Number)obj[1]).intValue()) {
				rating += ((Number)obj[0]).intValue() ;
				count++;
			}else {
				avgStarsPerRoom.put(roomId, rating/count );
				roomId = ((Number)obj[1]).intValue() ;
				rating = ((Number)obj[0]).intValue() ;
				count = 1;				
			}
		}
		return avgStarsPerRoom;
	}

	@Override
	public List<Integer> getRoomsByHotelSelectionWithDateSearch(int hotelIdSel, String in) {
		return bookingRepo.getRoomsByHotelSelectionWithDateSearch(hotelIdSel, in);
	}

	
	
}
