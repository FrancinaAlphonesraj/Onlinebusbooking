package com.guvi.Online.Bus.Booking.System.ServiceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.guvi.Online.Bus.Booking.System.entity.Booking;
import com.guvi.Online.Bus.Booking.System.entity.Bus;
import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.repo.BookingRepo;
import com.guvi.Online.Bus.Booking.System.service.AuthService;
import com.guvi.Online.Bus.Booking.System.service.BookingService;
import com.guvi.Online.Bus.Booking.System.service.PassengerService;

public class BookingServiceTest {

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private PassengerService passengerService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private BookingService bookingService;

    private Passenger mockPassenger;
    private Bus mockBus;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPassenger = new Passenger();
        mockPassenger.setPid(1L);
        mockPassenger.setName("John Doe");

        mockBus = new Bus();
        mockBus.setId(1L);
        mockBus.setBusNumber("123");
    }

    // Test bookSeats() - Successful booking
    @Test
    public void testBookSeats_Success() {
        when(authService.getCurrentPassenger()).thenReturn(mockPassenger);
        when(passengerService.findById(anyLong())).thenReturn(mockPassenger);

        bookingService.bookSeats(1L, 1L, Arrays.asList(1, 2, 3));

        verify(bookingRepo).save(any(Booking.class));
    }

    // Test getBookingsByBusId() - Return list of bookings by bus ID
    @Test
    public void testGetBookingsByBusId() {
        List<Booking> mockBookings = Arrays.asList(new Booking());
        when(bookingRepo.findByBusId(anyLong())).thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingsByBusId(1L);

        assertEquals(mockBookings, result);
    }

    // Test isSeatAvailable() - Seat is available
    @Test
    public void testIsSeatAvailable_True() {
        when(bookingRepo.findByBusId(anyLong())).thenReturn(Arrays.asList(new Booking()));

        boolean result = bookingService.isSeatAvailable(1L, 4);

        assertTrue(result);
    }

    // Test isSeatAvailable() - Seat is not available
    @Test
    public void testIsSeatAvailable_False() {
        Booking mockBooking = new Booking();
        mockBooking.setSeatNumbers(Arrays.asList(1, 2, 3));

        when(bookingRepo.findByBusId(anyLong())).thenReturn(Arrays.asList(mockBooking));

        boolean result = bookingService.isSeatAvailable(1L, 2);

        assertFalse(result);
    }

    // Test getByPassengerId() - Return bookings by passenger ID
    @Test
    public void testGetByPassengerId() {
        List<Booking> mockBookings = Arrays.asList(new Booking());
        when(bookingRepo.findByPassengerPid(anyLong())).thenReturn(mockBookings);

        List<Booking> result = bookingService.getByPassengerId(1L);

        assertEquals(mockBookings, result);
    }
}

