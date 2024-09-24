package com.guvi.Online.Bus.Booking.System.entity;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="booking")

public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
	
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
    
    @Column(name="passengername")
    private String passengerName;
    
    @Column(name="bookingtime")
    private LocalTime bookingTime;
    
    @ElementCollection
	private List<Integer> seatNumbers; // List of seat numbers being booked

	@Column(name = "booked")
	private boolean booked; // Whether the booking is complete

}
