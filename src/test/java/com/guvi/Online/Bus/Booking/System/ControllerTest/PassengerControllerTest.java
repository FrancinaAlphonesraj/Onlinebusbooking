package com.guvi.Online.Bus.Booking.System.ControllerTest;


import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.guvi.Online.Bus.Booking.System.Controller.PassengerController;
import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.service.AuthService;
import com.guvi.Online.Bus.Booking.System.service.PassengerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

class PassengerControllerTest {

    @Mock
    private PassengerService passengerService;

    @Mock
    private AuthService authService;

    @Mock
    private Model model;

    @InjectMocks
    private PassengerController passengerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(passengerController).build();
    }

    @Test
    public void testViewLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginform"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setName("John");
        when(authService.authenticate("john", "password")).thenReturn(passenger);

        mockMvc.perform(post("/login")
                .param("userName", "john")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("base-passenger"));
    }

    @Test
    public void testLoginAdminSuccess() throws Exception {
        mockMvc.perform(post("/login")
                .param("userName", "admin")
                .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("base-admin.html"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        when(authService.authenticate("john", "wrongpassword")).thenReturn(null);

        mockMvc.perform(post("/login")
                .param("userName", "john")
                .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Invalid username or password"))
                .andExpect(view().name("loginform"));
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("passenger-register"));
    }

    @Test
    public void testRegisterPassenger() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setName("John");

        mockMvc.perform(post("/createPassenger")
                .flashAttr("passenger", passenger))
                .andExpect(status().isOk())
                .andExpect(view().name("/loginform"));
        
        verify(passengerService).savePassenger(passenger);
    }

    @Test
    public void testShowProfile() throws Exception {
        Passenger passenger = new Passenger();
        when(authService.getCurrentPassenger()).thenReturn(passenger);

        mockMvc.perform(get("/passenger/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("passenger-profile"));
    }

    @Test
    public void testListPassengers() throws Exception {
        List<Passenger> passengers = Collections.emptyList();
        when(passengerService.getAll()).thenReturn(passengers);

        mockMvc.perform(get("/passenger/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("passenger-list"));
    }

    @Test
    public void testDeletePassenger() throws Exception {
        Long pid = 1L;

        mockMvc.perform(get("/delete/{id}", pid))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/passenger/list"));

        verify(passengerService).deletePassenger(pid);
    }
}

