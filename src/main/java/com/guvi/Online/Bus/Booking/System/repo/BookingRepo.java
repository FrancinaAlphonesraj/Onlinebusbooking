
package com.guvi.Online.Bus.Booking.System.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guvi.Online.Bus.Booking.System.entity.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
	List<Booking> findByPassengerPid(Long pid);
	
   List <Booking> findByBusId(Long busId);
}
