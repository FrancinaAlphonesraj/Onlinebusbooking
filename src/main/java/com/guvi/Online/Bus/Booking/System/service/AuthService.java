package com.guvi.Online.Bus.Booking.System.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.guvi.Online.Bus.Booking.System.entity.Passenger;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {
	
	@Autowired
	private PassengerService passengerServ;
	
	@Autowired
    private HttpSession session;
	
	 public Long getCurrentLoggedInPassengerId() {
	        // Retrieve the current HTTP session
	        HttpSession session = getCurrentSession();
	        if (session == null) {
	            return null;
	        }

	        // Get the username from the session
	        String username = (String) session.getAttribute("username");
	        if (username == null) {
	            return null;
	        }

	        // Find and return the Passenger by username
	        Passenger passenger = passengerServ.findPassengerByEmail(username);
	        return passenger != null ? passenger.getPid() : null;
	    }

	    // Method to retrieve the current HTTP session
	    private HttpSession getCurrentSession() {
	        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	        if (attributes != null) {
	            return attributes.getRequest().getSession(false);
	        }
	        return null;
	    }

	    // Method to authenticate a user and store the username in the session
	    public boolean authenticateUser(String username, String password) {
	        Passenger passenger = passengerServ.findPassengerByEmail(username);

	        if (passenger != null && passenger.getPassword().equals(password)) {
	            // Store username in session
	            HttpSession session = getCurrentSession();
	            if (session != null) {
	                session.setAttribute("username", username);
	            }
	            return true;
	        }
	        return false;
	    }

	
	
	public Passenger authenticate(String userName, String password) {
		 
		Passenger passenger = passengerServ.findPassengerByEmail(userName);
		if(passenger != null && passenger.getPassword().equals(password)) {
			session.setAttribute("LoggedInPassenger", passenger);
			return passenger;
		}
		
		return null;
		
	}
	
	
	public Passenger getCurrentPassenger() {
		return(Passenger) session.getAttribute("LoggedInPassenger");
	}
	
	
	public void logout() {
		 session.invalidate();
		
	}

}
