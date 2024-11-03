package fr.tpreservation.api;

import fr.tpreservation.model.Flight;
import fr.tpreservation.model.FlightReservation;
import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.FlightReservationRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FlightReservationControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightReservationRepository repository;

    /*
     * GET /flight-reservation
     */

    @Test
    @WithMockUser
    void shouldFindAllFlightReservationsWhenLogged() throws Exception {
        Utilisateur user = new Utilisateur();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@est.com");
        user.setPassword("password");

        Flight flight = new Flight();
        flight.setId("1");
        flight.setFlightNumber("AF123");
        flight.setDeparture("Paris");
        flight.setArrival("New York");
        flight.setDepartureTime(LocalDateTime.of(2023, 10, 1, 10, 0));
        flight.setArrivalTime(LocalDateTime.of(2023, 10, 1, 14, 0));

        FlightReservation flightReservation1 = new FlightReservation();
        flightReservation1.setId("1");
        flightReservation1.setFlight(flight);
        flightReservation1.setUser(user);
        flightReservation1.setReservationDate(LocalDateTime.of(2023, 10, 10, 10, 0));

        List<FlightReservation> flightReservations = List.of(flightReservation1);

        Mockito.when(this.repository.findAll()).thenReturn(flightReservations);

        this.mockMvc.perform(get("/flight-reservation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].reservationDate", Matchers.is("2023-10-10T10:00:00")));

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    @Test
    @WithMockUser
    void shouldFindAllFlightReservationsReturnsEmptyList() throws Exception {
        Mockito.when(this.repository.findAll()).thenReturn(List.of());

        this.mockMvc.perform(get("/flight-reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLogged() throws Exception {
        this.mockMvc.perform(get("/flight-reservation"))
                .andExpect(status().isUnauthorized());

        Mockito.verify(this.repository, Mockito.never()).findAll();
    }

    /*
     * POST /flight-reservation
     */


    /*
     * PUT /flight-reservation
     */


    /*
     * DELETE /flight-reservation
     */
}