package com.guvi.Online.Bus.Booking.System.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guvi.Online.Bus.Booking.System.entity.Passenger;

@Repository
public interface PassengerRepo extends JpaRepository<Passenger, Long> {
	
	Passenger findByUserName(String userName);
	
}
