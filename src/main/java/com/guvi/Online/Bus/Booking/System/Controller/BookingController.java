package com.guvi.Online.Bus.Booking.System.Controller;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.guvi.Online.Bus.Booking.System.entity.Booking;
import com.guvi.Online.Bus.Booking.System.entity.Bus;
import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.repo.BookingRepo;
import com.guvi.Online.Bus.Booking.System.service.AuthService;
import com.guvi.Online.Bus.Booking.System.service.BookingService;
import com.guvi.Online.Bus.Booking.System.service.BusService;
import com.guvi.Online.Bus.Booking.System.service.PassengerService;

@Controller
public class BookingController {
	
	@Autowired
	private BusService busService;
	
	 @Autowired
	    private AuthService authService;
	 
	 @Autowired
	 private BookingService bookingservice;
	 
	 
	 @Autowired
	 private BookingRepo bookingRepo;
	 @Autowired
	 private PassengerService passengerservice;
	
	@GetMapping("/select-seats/{busId}")
    public String showSeatSelection(@PathVariable Long id, Model model) {
        Bus bus = busService.getById(id);
        List<Integer> bookedSeats = busService.getBookedSeats(id);

        model.addAttribute("bus", bus);
        model.addAttribute("bookedSeats", bookedSeats);
        return "select-seats";
    }

    


	
	@PostMapping("/book-seats")
	public String bookSeats(@RequestParam("id") Long id, Booking booking,
	                        @RequestParam(value = "seats") String selectedSeats) {

	    Passenger currentPassenger = authService.getCurrentPassenger();
	    Passenger passenger = passengerservice.findById(currentPassenger.getPid());

	    // Set passenger details in the booking entity
	    booking.setPassenger(passenger);  
	    booking.setPassengerName(passenger.getName());

	    if (selectedSeats == null || selectedSeats.isEmpty()) {
	        return "redirect:/seat-selection?error=NoSeatsSelected";
	    }

	    System.out.println("Bus ID: " + id);
	    System.out.println("Selected Seats: " + selectedSeats);

	    // Convert selectedSeats (String) to List<Integer>
	    List<Integer> seatNumbers = Arrays.stream(selectedSeats.split(","))
	                                      .map(Integer::parseInt)
	                                      .collect(Collectors.toList());

	    // Call busService to handle seat booking
	    busService.bookSeats(id, seatNumbers, passenger.getPid());

	    // Save booking
	    booking.setBus(new Bus(id));
        booking.setBookingTime(LocalTime.now());

	    booking.setSeatNumbers(seatNumbers);  // Store seat numbers in booking
	    booking.setBooked(true);  // Mark the booking as complete

	    bookingRepo.save(booking);  // Save booking to the database

	    return "redirect:/confirmation";
	}

	@GetMapping("/confirmation")
	public String showBookingConfirmation(Model model) {
	    List<Booking> bookings = bookingservice.getByPassengerId(authService.getCurrentPassenger().getPid());
	    model.addAttribute("bookings", bookings); // Pass it as "bookings"
	    
	    return "booking-confirmation";
	}
    

}
