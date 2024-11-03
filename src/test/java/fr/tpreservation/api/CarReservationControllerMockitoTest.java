package fr.tpreservation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.tpreservation.model.Car;
import fr.tpreservation.model.CarReservation;
import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.CarReservationRepository;
import fr.tpreservation.request.CarReservation.PostCarReservationRequest;
import fr.tpreservation.request.CarReservation.PutCarReservationRequest;
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
public class CarReservationControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarReservationRepository repository;

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

    private Car createCar() {
        Car car = new Car();
        car.setId("1");
        car.setModel("Toyota");
        car.setBrand("Toyota");
        car.setCapacity(5);
        car.setPricePerDay(100);
        car.setLocation("Paris");
        car.setFuelType("Petrol");
        car.setDescription("A comfortable and spacious car.");
        car.setAvailable(true);
        return car;
    }

    private CarReservation createCarReservation(Utilisateur user, Car car) {
        CarReservation reservation = new CarReservation();
        reservation.setId("1");
        reservation.setCar(car);
        reservation.setUser(user);
        reservation.setDepartureTime(LocalDateTime.of(2023, 10, 10, 10, 0));
        reservation.setArrivalTime(LocalDateTime.of(2023, 10, 10, 10, 0));
        reservation.setReservationDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        return reservation;
    }

    /*
     * GET /car-reservation
     */

    @Test
    @WithMockUser
    void shouldFindAllCarReservationsWhenLogged() throws Exception {
        Utilisateur user = createUser();
        Car car = createCar();
        CarReservation reservation = createCarReservation(user, car);

        Mockito.when(repository.findAll()).thenReturn(List.of(reservation));

        mockMvc.perform(get("/car-reservation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(jsonPath("$[0].utilisateurId", Matchers.is("1")));

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    @WithMockUser
    void shouldFindAllCarReservationsReturnsEmptyList() throws Exception {
        Mockito.when(repository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/car-reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenCarReservationByIdDoesNotExist() throws Exception {
        Mockito.when(repository.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/car-reservation/999"))
                .andExpect(status().isNotFound());

        Mockito.verify(repository, Mockito.times(1)).findById("999");
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLogged() throws Exception {
        mockMvc.perform(get("/car-reservation"))
                .andExpect(status().isUnauthorized());

        Mockito.verify(repository, Mockito.never()).findAll();
    }

    /*
     * POST /car-reservation
     */

    @Test
    @WithMockUser
    void shouldAddCarReservation() throws Exception {
        setupObjectMapper();
        Utilisateur user = createUser();
        Car car = createCar();

        PostCarReservationRequest request = new PostCarReservationRequest();
        request.setCarId("1");
        request.setUserId("1");
        request.setStartDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        request.setEndDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        request.setTotalPrice(100);

        CarReservation reservation = createCarReservation(user, car);

        Mockito.when(repository.save(Mockito.any(CarReservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/car-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.utilisateurId", Matchers.is("1")));

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(CarReservation.class));
    }


    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCreatingCarReservationWithMissingFields() throws Exception {
        setupObjectMapper();
        PostCarReservationRequest request = new PostCarReservationRequest();
        // Missing required fields

        mockMvc.perform(post("/car-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PUT /car-reservation
     */

    @Test
    @WithMockUser
    void shouldUpdateCarReservation() throws Exception {
        setupObjectMapper();
        Utilisateur user = createUser();
        Car car = createCar();
        CarReservation existingReservation = createCarReservation(user, car);

        PutCarReservationRequest request = new PutCarReservationRequest();
        request.setStartDate(LocalDateTime.of(2023, 10, 11, 10, 0));
        request.setEndDate(LocalDateTime.of(2023, 10, 12, 10, 0));
        request.setTotalPrice(200);

        CarReservation updatedReservation = createCarReservation(user, car);
        updatedReservation.setDepartureTime(LocalDateTime.of(2023, 10, 11, 10, 0));
        updatedReservation.setArrivalTime(LocalDateTime.of(2023, 10, 12, 10, 0));

        Mockito.when(repository.findById("1")).thenReturn(Optional.of(existingReservation));
        Mockito.when(repository.save(Mockito.any(CarReservation.class))).thenReturn(updatedReservation);

        mockMvc.perform(put("/car-reservation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.utilisateurId", Matchers.is("1")));

        Mockito.verify(repository, Mockito.times(1)).findById("1");
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(CarReservation.class));
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenUpdatingNonExistentCarReservation() throws Exception {
        setupObjectMapper();
        PutCarReservationRequest request = new PutCarReservationRequest();
        request.setStartDate(LocalDateTime.of(2023, 10, 11, 10, 0));
        request.setEndDate(LocalDateTime.of(2023, 10, 12, 10, 0));
        request.setTotalPrice(200);

        Mockito.when(repository.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(put("/car-reservation/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        Mockito.verify(repository, Mockito.times(1)).findById("999");
    }

    /*
     * DELETE /car-reservation
     */

    @Test
    @WithMockUser
    void shouldDeleteCarReservation() throws Exception {
        Mockito.when(repository.existsById("1")).thenReturn(true);

        mockMvc.perform(delete("/car-reservation/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(repository, Mockito.times(1)).existsById("1");
        Mockito.verify(repository, Mockito.times(1)).deleteById("1");
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenDeletingNonExistentCarReservation() throws Exception {
        Mockito.when(repository.existsById("999")).thenReturn(false);

        mockMvc.perform(delete("/car-reservation/999"))
                .andExpect(status().isNotFound());

        Mockito.verify(repository, Mockito.times(1)).existsById("999");
    }
}