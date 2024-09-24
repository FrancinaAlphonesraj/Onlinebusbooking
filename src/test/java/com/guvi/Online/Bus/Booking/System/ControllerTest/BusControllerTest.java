package com.guvi.Online.Bus.Booking.System.ControllerTest;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.guvi.Online.Bus.Booking.System.Controller.BusController;
import com.guvi.Online.Bus.Booking.System.entity.Bus;
import com.guvi.Online.Bus.Booking.System.service.AuthService;
import com.guvi.Online.Bus.Booking.System.service.BookingService;
import com.guvi.Online.Bus.Booking.System.service.BusService;
import java.util.ArrayList;
import java.util.List;

class BusControllerTest {

    @Mock
    private BusService busServ;

    @Mock
    private BookingService bookingService;

    @Mock
    private AuthService authService;

    @Mock
    private Model model;

    @InjectMocks
    private BusController busController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testListBuses() {
        List<Bus> buses = new ArrayList<>();
        when(busServ.getAll()).thenReturn(buses);

        String view = busController.listDoctors(model);

        assertEquals("bus-list", view);
        verify(model).addAttribute("buses", buses);
    }

    @Test
    void testShowNewBusForm() {
        String view = busController.showBus(model);
        assertEquals("bus-new", view);
        verify(model).addAttribute(eq("bus"), any(Bus.class));
    }

    @Test
    void testSaveBus() {
        Bus bus = new Bus();
        String view = busController.saveBus(bus);
        assertEquals("redirect:/bus/list", view);
        verify(busServ).saveBus(bus);
    }

    @Test
    void testShowEditForm() {
        Long busId = 1L;
        Bus bus = new Bus();
        when(busServ.getById(busId)).thenReturn(bus);

        String view = busController.showEditForm(busId, model);

        assertEquals("bus-edit", view);
        verify(model).addAttribute("bus", bus);
    }

    @Test
    void testDeleteBus() {
        Long busId = 1L;

        String view = busController.deleteBus(busId);

        assertEquals("redirect:/bus/list", view);
        verify(busServ).deleteBus(busId);
    }

    @Test
    void testSearchBuses_noBuses() {
        String fromLocation = "CityA";
        String toLocation = "CityB";
        List<Bus> emptyBusList = new ArrayList<>();
        when(busServ.findBuses(fromLocation, toLocation)).thenReturn(emptyBusList);

        String view = busController.searchBuses(fromLocation, toLocation, model);

        assertEquals("booking", view);
        verify(model).addAttribute(eq("message"), anyString());
    }

    @Test
    void testSearchBuses_busesFound() {
        String fromLocation = "CityA";
        String toLocation = "CityB";
        List<Bus> buses = new ArrayList<>();
        buses.add(new Bus());
        when(busServ.findBuses(fromLocation, toLocation)).thenReturn(buses);

        String view = busController.searchBuses(fromLocation, toLocation, model);

        assertEquals("bus-view", view);
        verify(model).addAttribute("buses", buses);
    }
}
