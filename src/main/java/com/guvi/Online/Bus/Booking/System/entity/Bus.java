package com.guvi.Online.Bus.Booking.System.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bus")
public class Bus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "busnumber")
	private String busNumber;

	@Column(name = "fromlocation")
	private String fromLocation;

	@Column(name = "tolocation")
	private String toLocation;

	@Column(name = "arrivaltime")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime arrivalTime;

	@Column(name = "depaturetime")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime depatureTime;

	@Column(name = "capacity")
	private int capacity;

	@OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings; // List of bookings associated with the bus

	// Constructor accepting only the bus ID
    public Bus(Long id) {
        this.id = id;
    }
}
