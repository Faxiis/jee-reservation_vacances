package fr.tpreservation.api;

import fr.tpreservation.model.Hotel;
import fr.tpreservation.model.HotelReservation;
import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.HostelReservationRepository;
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
public class HostelReservationControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HostelReservationRepository repository;

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

        HotelReservation hostelReservation1 = new HotelReservation();
        hostelReservation1.setId("1");
        hostelReservation1.setUser(user);
        hostelReservation1.setReservationDate(LocalDateTime.of(2023, 10, 10, 10, 0));

        List<HotelReservation> hostelReservations = List.of(hostelReservation1);

        Mockito.when(this.repository.findAll()).thenReturn(hostelReservations);

        this.mockMvc.perform(get("/hostel-reservation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].reservationDate", Matchers.is("2023-10-10T10:00:00")));

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


    /*
     * PUT /hostel-reservation
     */


    /*
     * DELETE /hostel-reservation
     */
}