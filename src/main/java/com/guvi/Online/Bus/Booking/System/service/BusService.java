package com.guvi.Online.Bus.Booking.System.service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guvi.Online.Bus.Booking.System.entity.Booking;
import com.guvi.Online.Bus.Booking.System.entity.Bus;
import com.guvi.Online.Bus.Booking.System.repo.BookingRepo;
import com.guvi.Online.Bus.Booking.System.repo.BusRepo;

@Service
public class BusService {

	@Autowired
	private BusRepo busrepo;

	@Autowired
	private BookingRepo bookingRepository;

	public List<Bus> getAll() {
		return busrepo.findAll();
	}

	public Bus getById(Long id) {
		return busrepo.findById(id).orElse(null);
	}

	public void saveBus(Bus bus) {
		busrepo.save(bus);
	}

	public void deleteBus(Long id) {
		busrepo.deleteById(id);
	}
	

	 public List<Bus> findBuses(String fromLocation, String toLocation) {
//	        LocalDate travelDate = LocalDate.parse(date);
	        return busrepo.findByFromLocationAndToLocation(fromLocation, toLocation);
	    }
	public List<Integer> getBookedSeats(Long busId) {
		Bus bus = getById(busId);
		return bus.getBookings().stream().filter(Booking::isBooked)
				.flatMap(booking -> booking.getSeatNumbers().stream()) // FlatMap to collect all booked seats
				.collect(Collectors.toList());
	}

	public void bookSeats(Long id, List<Integer> seatNumbers, Long passengerId) {
		Bus bus = getById(id);
		Booking booking = new Booking();
		booking.setBus(bus);
		booking.setSeatNumbers(seatNumbers);
		booking.setId(id);
		booking.setBooked(true);
		booking.setBookingTime(LocalTime.now());

		bookingRepository.save(booking);
	}
}
