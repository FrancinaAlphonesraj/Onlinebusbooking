package com.guvi.Online.Bus.Booking.System.ControllerTest;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.guvi.Online.Bus.Booking.System.Controller.BookingController;
import com.guvi.Online.Bus.Booking.System.entity.Booking;
import com.guvi.Online.Bus.Booking.System.entity.Bus;
import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.repo.BookingRepo;
import com.guvi.Online.Bus.Booking.System.service.AuthService;
import com.guvi.Online.Bus.Booking.System.service.BookingService;
import com.guvi.Online.Bus.Booking.System.service.BusService;
import com.guvi.Online.Bus.Booking.System.service.PassengerService;

public class BookingControllerTest {

    @Mock
    private BusService busService;

    @Mock
    private AuthService authService;

    @Mock
    private BookingService bookingservice;

    @Mock
    private PassengerService passengerservice;

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private Model model;

    @InjectMocks
    private BookingController bookingController;

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

    // Test showSeatSelection() - Successful seat retrieval
    @Test
    public void testShowSeatSelection() {
        when(busService.getById(anyLong())).thenReturn(mockBus);
        when(busService.getBookedSeats(anyLong())).thenReturn(Arrays.asList(1, 2, 3));

        String viewName = bookingController.showSeatSelection(1L, model);

        assertEquals("select-seats", viewName);
        verify(model).addAttribute("bus", mockBus);
        verify(model).addAttribute("bookedSeats", Arrays.asList(1, 2, 3));
    }

    // Test bookSeats() - Successful seat booking
    @Test
    public void testBookSeats_Success() {
        when(authService.getCurrentPassenger()).thenReturn(mockPassenger);
        when(passengerservice.findById(anyLong())).thenReturn(mockPassenger);

        String viewName = bookingController.bookSeats(1L, new Booking(), "1,2,3");

        assertEquals("redirect:/confirmation", viewName);
        verify(busService).bookSeats(anyLong(), anyList(), anyLong());
        verify(bookingRepo).save(any(Booking.class));
    }

    // Test bookSeats() - No seats selected
    @Test
    public void testBookSeats_NoSeatsSelected() {
        String viewName = bookingController.bookSeats(1L, new Booking(), "");

        assertEquals("redirect:/seat-selection?error=NoSeatsSelected", viewName);
        verify(busService, never()).bookSeats(anyLong(), anyList(), anyLong());
    }

    // Test showBookingConfirmation() - Successful booking retrieval
    @Test
    public void testShowBookingConfirmation() {
        when(authService.getCurrentPassenger()).thenReturn(mockPassenger);
        List<Booking> mockBookings = Arrays.asList(new Booking());
        when(bookingservice.getByPassengerId(anyLong())).thenReturn(mockBookings);

        String viewName = bookingController.showBookingConfirmation(model);

        assertEquals("booking-confirmation", viewName);
        verify(model).addAttribute("bookings", mockBookings);
    }
}

