package com.synex.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.synex.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
	
	@Query(value = "select hotel_hotelid, hotelrooms_roomid from hotels_rooms where hotelrooms_roomId not in " + 
			"    (select roomId from booking " + 
			"    where to_date(?2, 'YYYY-MM-DD') <= checkIn or to_date(?1, 'YYYY-MM-DD') >= checkOut ) " + 
			"    order by hotel_hotelid", nativeQuery = true)
	public List<Object[]> findRoomIdsByInOutDate(String in, String out);
	
	
	@Query(value = "select distinct hotel_hotelid from hotels_rooms where hotelrooms_roomId not in " + 
			"    (select roomId from booking " + 
			"    where to_date(?2, 'YYYY-MM-DD') <= checkIn or to_date(?1, 'YYYY-MM-DD') >= checkOut ) " + 
			"    order by hotel_hotelid", nativeQuery = true)
	public List<Integer> findHotelIdsByInOutDate(String in, String out);
	
	
	@Query(value = "select distinct hotel_hotelid from hotels_rooms where hotelrooms_roomId not in " + 
			"    (select roomId from booking " + 
			"    where to_date(?2, 'YYYY-MM-DD') <= checkIn or to_date(?1, 'YYYY-MM-DD') >= checkOut ) " + 
			"    order by hotel_hotelid", nativeQuery = true)
	public List<Integer> findRoomsIdsByInOutDate(String in, String out);
	
	
	@Query(value="select * from Booking where userId=?1 and status='pending' ", nativeQuery=true)
	public List<Booking> findBookingByUserId(long userId);
	
	@Query(value = "update booking set status='complete' where userId=?1 and status='pending' and checkIn <= SYSDATE", nativeQuery=true)
	public void updateStatusByUserId(long userId);
	
	
	
	@Query(value = "select r.starrating, b.roomId from reviews r, booking b, booking_reviews br where " + 
			"    b.bookingid = br.booking_bookingid and r.feedbackid = br.reviews_feedbackid order by b.roomId", nativeQuery=true)
	public List<Object[]>  calculateAverageStarRatingHelper();
	
	
	@Query(value = "select hotelrooms_roomid from hotels_rooms where  hotel_hotelid=?1 minus " + 
			" select roomid from booking where hotelid=?1 " + 
			" sand ?2 >= to_char(checkin, 'YYYY-MM-DD') " + 
			" and ?2 <= to_char(checkout, 'YYYY-MM-DD')", nativeQuery = true)
	public List<Integer> getRoomsByHotelSelectionWithDateSearch(int hotelIdSel,String in);
	
}

