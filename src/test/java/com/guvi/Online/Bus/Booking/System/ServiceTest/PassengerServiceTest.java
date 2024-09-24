package com.guvi.Online.Bus.Booking.System.ServiceTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.repo.PassengerRepo;
import com.guvi.Online.Bus.Booking.System.service.PassengerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class PassengerServiceTest {

    @Mock
    private PassengerRepo passengerRepo;

    @InjectMocks
    private PassengerService passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Passenger> passengers = Arrays.asList(new Passenger(), new Passenger());
        when(passengerRepo.findAll()).thenReturn(passengers);

        List<Passenger> result = passengerService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void testFindPassengerByEmail() {
        Passenger passenger = new Passenger();
        when(passengerRepo.findByUserName("test")).thenReturn(passenger);

        Passenger result = passengerService.findPassengerByEmail("test");

        assertNotNull(result);
        assertEquals(passenger, result);
    }

    @Test
    void testSavePassenger() {
        Passenger passenger = new Passenger();
        passengerService.savePassenger(passenger);

        verify(passengerRepo).save(passenger);
    }

    @Test
    void testFindById() {
        Passenger passenger = new Passenger();
        when(passengerRepo.findById(1L)).thenReturn(Optional.of(passenger));

        Passenger result = passengerService.findById(1L);

        assertNotNull(result);
        assertEquals(passenger, result);
    }

    @Test
    void testFindById_NotFound() {
        when(passengerRepo.findById(1L)).thenReturn(Optional.empty());

        Passenger result = passengerService.findById(1L);

        assertNull(result);
    }

    @Test
    void testDeletePassenger() {
        Long pid = 1L;

        passengerService.deletePassenger(pid);

        verify(passengerRepo).deleteById(pid);
    }
}

