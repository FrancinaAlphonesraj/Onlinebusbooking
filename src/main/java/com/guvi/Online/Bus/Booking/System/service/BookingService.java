package com.guvi.Online.Bus.Booking.System.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guvi.Online.Bus.Booking.System.entity.Booking;
import com.guvi.Online.Bus.Booking.System.entity.Bus;
import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.repo.BookingRepo;

@Service
public class BookingService {
	
	@Autowired
    private BookingRepo bookingRepo;
	
	@Autowired
	private PassengerService passengerService;
	
	@Autowired
    private AuthService authService;

    public List<Booking> getBookingsByBusId(Long busId) {
        return bookingRepo.findByBusId(busId);
    }

    public void bookSeats(Long id, Long pid, List<Integer> seatNumbers) {
    	
    	 Booking booking = new Booking();
    	 booking.setBus(new Bus(id)); //constructor for Bus with busId
    	 
    	// Retrieve the Passenger by its ID (assuming you have a PassengerService)
    	 Passenger passenger = passengerService.findById(authService.getCurrentPassenger().getPid());

    	 // Set the passenger object in the booking
    	 booking.setPassenger(passenger); 

         booking.setPassengerName(passenger.getName()); // Assuming a constructor for Passenger
         booking.setSeatNumbers(seatNumbers);
         booking.setBookingTime(LocalTime.now());
         booking.setBooked(true);
         bookingRepo.save(booking);
    }
    
    public boolean isSeatAvailable(Long id, Integer seatNumber) {
        List<Booking> existingBookings = bookingRepo.findByBusId(id);
        for (Booking booking : existingBookings) {
            if (booking.getSeatNumbers().contains(seatNumber)) {
                return false; // Seat is already booked
            }
        }
        return true; // Seat is available
    }
    
   public List<Booking> getByPassengerId(Long pid){
	   return bookingRepo.findByPassengerPid(pid);
   }
}
