package com.guvi.Online.Bus.Booking.System.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="passenger")
public class Passenger {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pid;
	
	@Column(name="name")
	private String name;
	
	@Column(name="age")
	private int age;
	
	@Column(name="location")
	private String address;
	
	@Column(name="username")
	private  String userName;
	
	@Column(name="password")
	private String password;

	public String getName() {
        return this.name;
    }
}
