package fr.tpreservation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.tpreservation.model.Flight;
import fr.tpreservation.model.FlightReservation;
import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.FlightReservationRepository;
import fr.tpreservation.request.FlightReservation.PostFlightReservationRequest;
import fr.tpreservation.request.FlightReservation.PutFlightReservationRequest;
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
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FlightReservationControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightReservationRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private void setupObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    private Utilisateur createUser() {
        Utilisateur user = new Utilisateur();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@est.com");
        user.setPassword("password");
        return user;
    }

    private Flight createFlight() {
        Flight flight = new Flight();
        flight.setId("1");
        flight.setFlightNumber("AF123");
        flight.setDeparture("Paris");
        flight.setArrival("New York");
        flight.setDepartureTime(LocalDateTime.of(2023, 10, 10, 10, 0));
        flight.setArrivalTime(LocalDateTime.of(2023, 10, 10, 14, 0));
        return flight;
    }

    private FlightReservation createFlightReservation(Utilisateur user, Flight flight) {
        FlightReservation reservation = new FlightReservation();
        reservation.setId("1");
        reservation.setFlight(flight);
        reservation.setUser(user);
        reservation.setReservationDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        return reservation;
    }

    /*
     * GET /flight-reservation
     */

    @Test
    @WithMockUser
    void shouldFindAllFlightReservationsWhenLogged() throws Exception {
        Utilisateur user = createUser();
        Flight flight = createFlight();
        FlightReservation reservation = createFlightReservation(user, flight);

        Mockito.when(repository.findAll()).thenReturn(List.of(reservation));

        mockMvc.perform(get("/flight-reservation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is("1")));

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    @WithMockUser
    void shouldFindAllFlightReservationsReturnsEmptyList() throws Exception {
        Mockito.when(repository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/flight-reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLogged() throws Exception {
        mockMvc.perform(get("/flight-reservation"))
                .andExpect(status().isUnauthorized());

        Mockito.verify(repository, Mockito.never()).findAll();
    }


    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenFlightReservationByIdDoesNotExist() throws Exception {
        Mockito.when(repository.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/flight-reservation/999"))
                .andExpect(status().isNotFound());

        Mockito.verify(repository, Mockito.times(1)).findById("999");
    }

    /*
     * POST /flight-reservation
     */

    @Test
    @WithMockUser
    void shouldAddFlightReservation() throws Exception {
        setupObjectMapper();
        Utilisateur user = createUser();
        Flight flight = createFlight();

        PostFlightReservationRequest request = new PostFlightReservationRequest();
        request.setFlightId("1");
        request.setUserId("1");
        request.setDepartureDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        request.setReturnDate(LocalDateTime.of(2023, 10, 11, 10, 0));
        request.setTotalPrice(150);

        FlightReservation reservation = createFlightReservation(user, flight);

        Mockito.when(repository.save(Mockito.any(FlightReservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/flight-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is("1")));

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(FlightReservation.class));
    }


    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCreatingFlightReservationWithMissingFields() throws Exception {
        setupObjectMapper();
        PostFlightReservationRequest request = new PostFlightReservationRequest();
        // Missing required fields

        mockMvc.perform(post("/flight-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PUT /flight-reservation
     */

    //@Test
    //@WithMockUser
    //void shouldUpdateFlightReservation() throws Exception {
    //    setupObjectMapper();
    //    Utilisateur user = createUser();
    //    Flight flight = createFlight();
    //    FlightReservation existingReservation = createFlightReservation(user, flight);
//
    //    PutFlightReservationRequest request = new PutFlightReservationRequest();
    //    request.setDepartureDate(LocalDateTime.of(2023, 10, 10, 10, 0));
    //    request.setReturnDate(LocalDateTime.of(2023, 10, 11, 10, 0));
//
    //    FlightReservation updatedReservation = createFlightReservation(user, flight);
    //    updatedReservation.setReservationDate(LocalDateTime.of(2023, 10, 11, 10, 0));
//
    //    Mockito.when(repository.findById("1")).thenReturn(Optional.of(existingReservation));
    //    Mockito.when(repository.save(Mockito.any(FlightReservation.class))).thenReturn(updatedReservation);
//
    //    mockMvc.perform(put("/flight-reservation/1")
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(objectMapper.writeValueAsString(request)))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //            .andExpect(jsonPath("$.id", Matchers.is("1")))
    //            .andExpect(jsonPath("$.utilisateurId", Matchers.is("1")));
//
    //    Mockito.verify(repository, Mockito.times(1)).findById("1");
    //    Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(FlightReservation.class));
    //}

    /*
     * DELETE /flight-reservation
     */

    @Test
    @WithMockUser
    void shouldDeleteFlightReservation() throws Exception {
        Mockito.when(repository.existsById("1")).thenReturn(true);

        mockMvc.perform(delete("/flight-reservation/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(repository, Mockito.times(1)).existsById("1");
        Mockito.verify(repository, Mockito.times(1)).deleteById("1");
    }


    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenDeletingNonExistentFlightReservation() throws Exception {
        Mockito.when(repository.existsById("999")).thenReturn(false);

        mockMvc.perform(delete("/flight-reservation/999"))
                .andExpect(status().isNotFound());

        Mockito.verify(repository, Mockito.times(1)).existsById("999");
    }

}