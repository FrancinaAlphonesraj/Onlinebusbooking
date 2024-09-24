package com.guvi.Online.Bus.Booking.System.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guvi.Online.Bus.Booking.System.entity.Bus;

public interface BusRepo extends JpaRepository<Bus, Long> {
    List<Bus> findByFromLocationAndToLocation(String fromLocation, String toLocation);

}
