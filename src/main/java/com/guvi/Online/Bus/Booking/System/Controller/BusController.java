package com.guvi.Online.Bus.Booking.System.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.guvi.Online.Bus.Booking.System.entity.Booking;
import com.guvi.Online.Bus.Booking.System.entity.Bus;
import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.service.AuthService;
import com.guvi.Online.Bus.Booking.System.service.BookingService;
import com.guvi.Online.Bus.Booking.System.service.BusService;
import lombok.*;


@Controller
@RequiredArgsConstructor
public class BusController {
	
	@Autowired
	private BusService busServ;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private AuthService authService;
	
	@GetMapping("/bus/list")
	public String listDoctors(Model model) {
		List<Bus> buses = busServ.getAll();
		model.addAttribute("buses", buses);
		return"bus-list";
	}
	
	@GetMapping("/bus/new")
	public String showBus(Model model) {
		model.addAttribute("bus", new Bus());
		return"bus-new";
	}
	
	@PostMapping("/bus/create")
	public String saveBus(@ModelAttribute("bus") Bus bus) {
		busServ.saveBus(bus);
		return "redirect:/bus/list";
	}
	
	@GetMapping("/bus/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		Bus bus = busServ.getById(id);
		model.addAttribute("bus",bus);
		return"bus-edit";				
	}
		
	@GetMapping("/bus/delete/{id}")
	
	public String deleteBus(@PathVariable("id") Long id) {
		busServ.deleteBus(id);
		return"redirect:/bus/list";
	}
	
	@GetMapping("/booking")
    public String showBookingForm() {
        return "booking";
    }
	
	 @PostMapping("/booking/search")
	    public String searchBuses(@RequestParam("fromLocation") String fromLocation,
	                              @RequestParam("toLocation") String toLocation,
                                  Model model) {
	        List<Bus> buses = busServ.findBuses(fromLocation, toLocation);
	        if (buses.isEmpty()) {
	            model.addAttribute("message", "No bus available for the selected route and date.");
	            return "booking";
	        }
	        model.addAttribute("buses", buses);
	        return "bus-view";
	    }

	 @GetMapping("/booking/select-seats/{id}")
	    public String selectSeats(@PathVariable("id") Long id, Model model) {
//	        List<Booking> bookings = bookingService.getBookingsByBusId(id);
//	        model.addAttribute("id", id);
//	        Bus bus = busServ.getById(id);
//	        model.addAttribute("bus", bus);
//	        model.addAttribute("bookings", bookings);
//	        return "seat-selection";
		 
		 Bus bus = busServ.getById(id);

		    // Fetch bookings for the bus
		    List<Booking> bookings = bookingService.getBookingsByBusId(id);
		    List<Integer> bookedSeats = bookings.stream()
                    .flatMap(booking -> booking.getSeatNumbers().stream())  // Flatten the seat numbers list
                    .collect(Collectors.toList());

		    // Initialize bookedSeats to an empty list if no bookings are found
		    if (bookedSeats == null) {
		        bookedSeats = new ArrayList<>();
		    }

		    // Prepare list of all seats
		    List<Integer> allSeats = IntStream.rangeClosed(1, bus.getCapacity())
		                                      .boxed()
		                                      .collect(Collectors.toList());

		    List<Integer> availableSeats = allSeats.stream()
//                    .filter(seat -> !bookedSeats.contains(seat))
                    .collect(Collectors.toList());

		    // Ensure availableSeats is initialized, even if empty
		    if (availableSeats == null) {
		        availableSeats = new ArrayList<>();
		    }

		    model.addAttribute("bus", bus);
		    model.addAttribute("bookedSeats", bookedSeats);  // Non-null bookedSeats
		    model.addAttribute("availableSeats", availableSeats);  // Non-null availableSeats

		    return "seat-selection";
	    }
	 
	 @PostMapping("/booking/book-seats")
	    public String bookSeats(@RequestParam("id") Long id,
	    		@RequestParam(value = "pid", required = false) Long pid,
//	                            @RequestParam("pid") Long pid,
	                            @RequestParam("seats") List<Integer> seatNumbers,
	                            Model model) {
		 
		 // Get the current logged-in passenger ID from AuthService
	        Long passengerId = authService.getCurrentLoggedInPassengerId();
	        
	        if (passengerId == null) {
	            // Handle case where passenger ID is not found
	            return "redirect:/login?error=notloggedin";
	        }

	        bookingService.bookSeats(id, pid, seatNumbers);
	        return "redirect:/booking";
	    }
	 
	
}
