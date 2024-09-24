package com.guvi.Online.Bus.Booking.System.ServiceTest;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.guvi.Online.Bus.Booking.System.entity.Passenger;
import com.guvi.Online.Bus.Booking.System.service.AuthService;
import com.guvi.Online.Bus.Booking.System.service.PassengerService;




public class AuthServiceTest {
	
	 @Mock
	    private PassengerService passengerServ;

	    @Mock
	    private HttpSession session;

	    @InjectMocks
	    private AuthService authService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    // Test authenticateUser() - Successful Authentication
	    @Test
	    public void testAuthenticateUser_Success() {
	        Passenger mockPassenger = new Passenger();
	        mockPassenger.setUserName("test@example.com");
	        mockPassenger.setPassword("password");

	        when(passengerServ.findPassengerByEmail("test@example.com")).thenReturn(mockPassenger);
	        when(session.getAttribute("username")).thenReturn("test@example.com");

	        boolean result = authService.authenticateUser("test@example.com", "password");
	        
	        assertTrue(result);
	        verify(session).setAttribute("username", "test@example.com");
	    }

	    // Test authenticateUser() - Authentication Failure
	    @Test
	    public void testAuthenticateUser_Failure() {
	        when(passengerServ.findPassengerByEmail("wrong@example.com")).thenReturn(null);

	        boolean result = authService.authenticateUser("wrong@example.com", "password");
	        
	        assertFalse(result);
	        verify(session, never()).setAttribute(anyString(), any());
	    }

	    // Test getCurrentLoggedInPassengerId() - Successfully retrieving passenger ID
	    @Test
	    public void testGetCurrentLoggedInPassengerId_Success() {
	        Passenger mockPassenger = new Passenger();
	        mockPassenger.setPid(1L);
	        mockPassenger.setUserName("test@example.com");

	        when(session.getAttribute("username")).thenReturn("test@example.com");
	        when(passengerServ.findPassengerByEmail("test@example.com")).thenReturn(mockPassenger);

	        Long result = authService.getCurrentLoggedInPassengerId();

	        assertEquals(1L, result);
	    }

	    // Test getCurrentLoggedInPassengerId() - No username in session
	    @Test
	    public void testGetCurrentLoggedInPassengerId_NoUsername() {
	        when(session.getAttribute("username")).thenReturn(null);

	        Long result = authService.getCurrentLoggedInPassengerId();

	        assertNull(result);
	    }

	    // Test authenticate() - Success
	    @Test
	    public void testAuthenticate_Success() {
	        Passenger mockPassenger = new Passenger();
	        mockPassenger.setUserName("test@example.com");
	        mockPassenger.setPassword("password");

	        when(passengerServ.findPassengerByEmail("test@example.com")).thenReturn(mockPassenger);

	        Passenger result = authService.authenticate("test@example.com", "password");

	        assertNotNull(result);
	        assertEquals(mockPassenger, result);
	        verify(session).setAttribute("LoggedInPassenger", mockPassenger);
	    }

	    // Test authenticate() - Failure
	    @Test
	    public void testAuthenticate_Failure() {
	        when(passengerServ.findPassengerByEmail("wrong@example.com")).thenReturn(null);

	        Passenger result = authService.authenticate("wrong@example.com", "password");

	        assertNull(result);
	        verify(session, never()).setAttribute(anyString(), any());
	    }

	    // Test getCurrentPassenger() - Successfully retrieving passenger from session
	    @Test
	    public void testGetCurrentPassenger_Success() {
	        Passenger mockPassenger = new Passenger();
	        mockPassenger.setUserName("test@example.com");

	        when(session.getAttribute("LoggedInPassenger")).thenReturn(mockPassenger);

	        Passenger result = authService.getCurrentPassenger();

	        assertEquals(mockPassenger, result);
	    }

	    // Test logout() - Session invalidated
	    @Test
	    public void testLogout() {
	        authService.logout();
	        verify(session).invalidate();
	    }

}
