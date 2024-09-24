package com.guvi.Online.Bus.Booking.System.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.service.AuthService;
import com.guvi.Online.Bus.Booking.System.service.PassengerService;

@Controller

public class PassengerController {
	
	@Autowired
	private PassengerService passengerService;
	
	@Autowired
	private AuthService authService;
//	Show login form
	
	@GetMapping("/")
    public String viewLogin() {
        return "loginform";
    }
	
	@PostMapping("/login")
	public String login(@RequestParam String userName, @RequestParam String password, Model model) {
		Passenger passenger = authService.authenticate(userName, password);
		if(passenger != null) {
			passenger.getName();
					return "base-passenger";
		}else  if ("admin".equals(userName) && "admin".equals(password)) {
            return "base-admin.html";  // Redirect to admin page if admin credentials match
        }else{
			model.addAttribute("error", "Invalid username or password"); 
            return "loginform";
		}
	}
	
	@GetMapping("/register")
    public String showRegistrationForm(Model model) {	
	model.addAttribute("passenger", new Passenger());
		return"passenger-register";
	}
	
	@PostMapping("/createPassenger")
	
	public String registerPassenger(@ModelAttribute("passenger") Passenger passenger) {
		passengerService.savePassenger(passenger);
		return "/loginform";
	}
	
	@GetMapping("/passenger/profile")
	public String showProfile(Model model) {
		Passenger passenger = authService.getCurrentPassenger();
		model.addAttribute("passenger", passenger);
		return "passenger-profile";
	}
	
	
	@GetMapping("/passenger/findById/{id}")
	public String updateProfile(@PathVariable("id") Long pid, Model model ) {
		Passenger passengers = passengerService.findById(pid);
		model.addAttribute("passenger", passengers);
		return "passenger-edit";
	}
	
	@GetMapping("/passenger/list")
	public String listPassengers(Model model) {
		List<Passenger> passengers = passengerService.getAll();
		model.addAttribute("passengers", passengers);
		return"passenger-list";
	}
	
	@GetMapping("/delete/{id}")
	public String deletePassenger(@PathVariable("id") Long pid) {
		passengerService.deletePassenger(pid);
		return"redirect:/passenger/list";
	}
	
	
	 @GetMapping("/logout")
	    public String logout() {
	        authService.logout();
	        return "redirect:/";
	    }

	

}
