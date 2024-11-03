package fr.tpreservation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.tpreservation.model.Hotel;
import fr.tpreservation.model.HotelReservation;
import fr.tpreservation.model.Room;
import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.HostelReservationRepository;
import fr.tpreservation.request.HostelReservation.PostHostelReservationRequest;
import fr.tpreservation.request.HostelReservation.PutHostelReservationRequest;
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
public class HostelReservationControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HostelReservationRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private void setupObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /*
     * GET /hostel-reservation
     */

    @Test
    @WithMockUser
    void shouldFindAllHostelReservationsWhenLogged() throws Exception {
        Utilisateur user = new Utilisateur();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@est.com");
        user.setPassword("password");

        Hotel hostel = new Hotel();
        hostel.setId("1");
        hostel.setName("Hostel One");
        hostel.setAddress("Paris");

        Room room = new Room();
        room.setId("1");
        room.setHotel(hostel);
        room.setPrice(100);
        room.setName("Suite deluxe 5 personnes");


        HotelReservation hostelReservation1 = new HotelReservation();
        hostelReservation1.setId("1");
        hostelReservation1.setUser(user);
        hostelReservation1.setReservationDate(LocalDateTime.of(2023, 10, 10, 10, 0));

        List<HotelReservation> hostelReservations = List.of(hostelReservation1);

        Mockito.when(this.repository.findAll()).thenReturn(hostelReservations);

        this.mockMvc.perform(get("/hostel-reservation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)));

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    @Test
    @WithMockUser
    void shouldFindAllHostelReservationsReturnsEmptyList() throws Exception {
        Mockito.when(this.repository.findAll()).thenReturn(List.of());

        this.mockMvc.perform(get("/hostel-reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLogged() throws Exception {
        this.mockMvc.perform(get("/hostel-reservation"))
                .andExpect(status().isUnauthorized());

        Mockito.verify(this.repository, Mockito.never()).findAll();
    }

    /*
     * POST /hostel-reservation
     */

    @Test
    @WithMockUser
    void shouldCreateRoomReservation() throws Exception {
        setupObjectMapper();
        Utilisateur user = new Utilisateur();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@est.com");
        user.setPassword("password");

        Hotel hotel = new Hotel();
        hotel.setId("1");
        hotel.setName("Hotel One");
        hotel.setAddress("Paris");

        Room room = new Room();
        room.setId("1");
        room.setHotel(hotel);
        room.setPrice(100);
        room.setName("Suite deluxe 5 personnes");

        PostHostelReservationRequest request = new PostHostelReservationRequest();
        request.setRoomId("1");
        request.setUserId("1");
        request.setCheckInDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        request.setCheckOutDate(LocalDateTime.of(2023, 10, 12, 10, 0));
        request.setTotalPrice(200);

        HotelReservation reservation = new HotelReservation();
        reservation.setId("1");
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setReservationDate(LocalDateTime.of(2023, 10, 10, 10, 0));

        Mockito.when(repository.save(Mockito.any(HotelReservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/hostel-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is("1")));

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(HotelReservation.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCreatingRoomReservationWithMissingFields() throws Exception {
        setupObjectMapper();
        PostHostelReservationRequest request = new PostHostelReservationRequest();

        mockMvc.perform(post("/hostel-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLoggedForPost() throws Exception {
        setupObjectMapper();
        PostHostelReservationRequest request = new PostHostelReservationRequest();
        request.setRoomId("1");
        request.setUserId("1");
        request.setCheckInDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        request.setCheckOutDate(LocalDateTime.of(2023, 10, 12, 10, 0));
        request.setTotalPrice(200);

        mockMvc.perform(post("/hostel-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


    /*
     * PUT /hostel-reservation
     */

    @Test
    @WithMockUser
    void shouldUpdateRoomReservation() throws Exception {
        setupObjectMapper();
        Utilisateur user = new Utilisateur();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@est.com");
        user.setPassword("password");

        Hotel hotel = new Hotel();
        hotel.setId("1");
        hotel.setName("Hotel One");
        hotel.setAddress("Paris");

        Room room = new Room();
        room.setId("1");
        room.setHotel(hotel);
        room.setPrice(100);
        room.setName("Suite deluxe 5 personnes");

        HotelReservation existingReservation = new HotelReservation();
        existingReservation.setId("1");
        existingReservation.setUser(user);
        existingReservation.setRoom(room);
        existingReservation.setReservationDate(LocalDateTime.of(2023, 10, 10, 10, 0));

        PutHostelReservationRequest request = new PutHostelReservationRequest();
        request.setCheckInDate(LocalDateTime.of(2023, 10, 11, 10, 0));
        request.setCheckOutDate(LocalDateTime.of(2023, 10, 12, 10, 0));
        request.setTotalPrice(200);

        HotelReservation updatedReservation = new HotelReservation();
        updatedReservation.setId("1");
        updatedReservation.setUser(user);
        updatedReservation.setRoom(room);
        updatedReservation.setReservationDate(LocalDateTime.of(2023, 10, 11, 10, 0));

        Mockito.when(repository.findById("1")).thenReturn(Optional.of(existingReservation));
        Mockito.when(repository.save(Mockito.any(HotelReservation.class))).thenReturn(updatedReservation);

        mockMvc.perform(put("/hostel-reservation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is("1")));

        Mockito.verify(repository, Mockito.times(1)).findById("1");
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(HotelReservation.class));
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenUpdatingNonExistentRoomReservation() throws Exception {
        setupObjectMapper();
        PutHostelReservationRequest request = new PutHostelReservationRequest();
        request.setCheckInDate(LocalDateTime.of(2023, 10, 11, 10, 0));
        request.setCheckOutDate(LocalDateTime.of(2023, 10, 12, 10, 0));
        request.setTotalPrice(200);

        Mockito.when(repository.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(put("/hostel-reservation/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        Mockito.verify(repository, Mockito.times(1)).findById("999");
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLoggedForPut() throws Exception {
        setupObjectMapper();
        PutHostelReservationRequest request = new PutHostelReservationRequest();
        request.setCheckInDate(LocalDateTime.of(2023, 10, 11, 10, 0));
        request.setCheckOutDate(LocalDateTime.of(2023, 10, 12, 10, 0));
        request.setTotalPrice(200);

        mockMvc.perform(put("/hostel-reservation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    /*
     * DELETE /hostel-reservation
     */

    @Test
    @WithMockUser
    void shouldDeleteRoomReservation() throws Exception {
        Mockito.when(repository.existsById("1")).thenReturn(true);

        mockMvc.perform(delete("/hostel-reservation/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(repository, Mockito.times(1)).existsById("1");
        Mockito.verify(repository, Mockito.times(1)).deleteById("1");
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenDeletingNonExistentRoomReservation() throws Exception {
        Mockito.when(repository.existsById("999")).thenReturn(false);

        mockMvc.perform(delete("/hostel-reservation/999"))
                .andExpect(status().isNotFound());

        Mockito.verify(repository, Mockito.times(1)).existsById("999");
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLoggedForDelete() throws Exception {
        mockMvc.perform(delete("/hostel-reservation/1"))
                .andExpect(status().isUnauthorized());
    }
}