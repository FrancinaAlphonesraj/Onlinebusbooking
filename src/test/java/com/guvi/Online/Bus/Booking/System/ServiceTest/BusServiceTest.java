package com.guvi.Online.Bus.Booking.System.ServiceTest;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.guvi.Online.Bus.Booking.System.entity.Bus;
import com.guvi.Online.Bus.Booking.System.entity.Booking;
import com.guvi.Online.Bus.Booking.System.repo.BusRepo;
import com.guvi.Online.Bus.Booking.System.service.BusService;
import com.guvi.Online.Bus.Booking.System.repo.BookingRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class BusServiceTest {

    @Mock
    private BusRepo busrepo;

    @Mock
    private BookingRepo bookingRepository;

    @InjectMocks
    private BusService busService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAll() {
        List<Bus> buses = new ArrayList<>();
        when(busrepo.findAll()).thenReturn(buses);

        List<Bus> result = busService.getAll();

        assertEquals(buses, result);
    }

    @Test
    void testGetById_busFound() {
        Long busId = 1L;
        Bus bus = new Bus();
        when(busrepo.findById(busId)).thenReturn(Optional.of(bus));

        Bus result = busService.getById(busId);

        assertEquals(bus, result);
    }

    @Test
    void testGetById_busNotFound() {
        Long busId = 1L;
        when(busrepo.findById(busId)).thenReturn(Optional.empty());

        Bus result = busService.getById(busId);

        assertNull(result);
    }

    @Test
    void testSaveBus() {
        Bus bus = new Bus();
        busService.saveBus(bus);

        verify(busrepo).save(bus);
    }

    @Test
    void testDeleteBus() {
        Long busId = 1L;
        busService.deleteBus(busId);

        verify(busrepo).deleteById(busId);
    }

    @Test
    void testFindBuses() {
        String fromLocation = "CityA";
        String toLocation = "CityB";
        List<Bus> buses = new ArrayList<>();
        when(busrepo.findByFromLocationAndToLocation(fromLocation, toLocation)).thenReturn(buses);

        List<Bus> result = busService.findBuses(fromLocation, toLocation);

        assertEquals(buses, result);
    }

    @Test
    void testGetBookedSeats() {
        Long busId = 1L;
        Bus bus = new Bus();
        Booking booking = new Booking();
        List<Integer> seatNumbers = List.of(1, 2, 3);
        booking.setSeatNumbers(seatNumbers);
        bus.setBookings(List.of(booking));
        when(busrepo.findById(busId)).thenReturn(Optional.of(bus));

        List<Integer> result = busService.getBookedSeats(busId);

        assertEquals(seatNumbers, result);
    }

    @Test
    void testBookSeats() {
        Long busId = 1L;
        List<Integer> seatNumbers = List.of(1, 2, 3);
        Long passengerId = 100L;
        Bus bus = new Bus();
        when(busrepo.findById(busId)).thenReturn(Optional.of(bus));

        busService.bookSeats(busId, seatNumbers, passengerId);

        verify(bookingRepository).save(any(Booking.class));
    }
}

