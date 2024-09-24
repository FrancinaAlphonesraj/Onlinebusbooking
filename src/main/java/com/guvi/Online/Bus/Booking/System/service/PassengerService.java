package com.guvi.Online.Bus.Booking.System.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.repo.PassengerRepo;

@Service
public class PassengerService {
	
	@Autowired
    private PassengerRepo passengerRepo;
	
	public List<Passenger> getAll(){
		return passengerRepo.findAll();
		
	}
	
	public Passenger findPassengerByEmail(String userName) {
        return passengerRepo.findByUserName(userName);
    }
	
	public void savePassenger(Passenger passenger) {
		passengerRepo.save(passenger);
	}
	
	public Passenger findById(Long pid) {
		return passengerRepo.findById(pid).orElse(null);
	}
	
	public void deletePassenger(Long pid) {
		passengerRepo.deleteById(pid);
	}

}
