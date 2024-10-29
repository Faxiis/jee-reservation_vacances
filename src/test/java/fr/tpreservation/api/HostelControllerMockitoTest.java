package fr.tpreservation.api;

import java.util.List;
import java.util.Optional;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.tpreservation.model.Hotel;
import fr.tpreservation.repo.HostelRepository;
import fr.tpreservation.request.HostelRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class HostelControllerMockitoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HostelRepository repository;
    /*
     * GET /hotels
     */

    // Doit retourner une liste de tous les hôtels si l'utilisateur est connecté.
    @Test
    @WithMockUser
    void shouldFindAllHotelsWhenLogged() throws Exception {
        // given
        Hotel hotel1 = new Hotel();
        hotel1.setId("1");
        hotel1.setName("Luxury Inn");
        hotel1.setAddress("123 Luxury St");
        hotel1.setCity("Paris");
        hotel1.setCountry("France");
        hotel1.setStars(5);
        hotel1.setRooms(100);
        hotel1.setAvailableRooms(50);
        hotel1.setPrice(200);

        List<Hotel> hotels = List.of(hotel1);

        Mockito.when(this.repository.findAll()).thenReturn(hotels);

        // when
        this.mockMvc.perform(get("/hostels"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andExpect(jsonPath("$[0].name", Matchers.is("Luxury Inn")));

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    // Doit retourner une liste vide si aucun hôtel n'existe.
    @Test
    @WithMockUser
    void shouldFindAllHotelsReturnsEmptyList() throws Exception {
        // given
        Mockito.when(this.repository.findAll()).thenReturn(List.of());

        // when
        this.mockMvc.perform(get("/hostels"))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    // Doit retourner une erreur d'autorisation (401) si l'utilisateur n'est pas connecté.
    @Test
    void shouldReturnUnauthorizedWhenNotLogged() throws Exception {
        // when
        this.mockMvc.perform(get("/hostels"))
            // then
            .andExpect(status().isUnauthorized());

        Mockito.verify(this.repository, Mockito.never()).findAll();
    }

  /*
  * GET /hostels/{id}
  */

  // Doit retourner un hôtel par ID si celui-ci existe.
  @Test
  @WithMockUser
  void shouldReturnHotelByIdWhenExists() throws Exception {
      // given
      String hotelId = "1";
      Hotel hotel = new Hotel();
      hotel.setId(hotelId);
      hotel.setName("Luxury Inn");
      hotel.setAddress("123 Luxury St");
      hotel.setCity("Paris");
      hotel.setCountry("France");
      hotel.setStars(5);
      hotel.setRooms(100);
      hotel.setAvailableRooms(50);
      hotel.setPrice(200);

      Mockito.when(this.repository.findById(hotelId)).thenReturn(Optional.of(hotel));

      // when
      this.mockMvc.perform(get("/hostels/{id}", hotelId))
          // then
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.name", Matchers.is("Luxury Inn")))
          .andExpect(jsonPath("$.address", Matchers.is("123 Luxury St")))
          .andExpect(jsonPath("$.city", Matchers.is("Paris")))
          .andExpect(jsonPath("$.country", Matchers.is("France")))
          .andExpect(jsonPath("$.stars", Matchers.is(5)));

      Mockito.verify(this.repository, Mockito.times(1)).findById(hotelId);
  }

  // Doit retourner une erreur 404 si l'hôtel n'existe pas.
  @Test
  @WithMockUser
  void shouldReturnNotFoundWhenHotelDoesNotExist() throws Exception {
      // given
      String hotelId = "999";
      Mockito.when(this.repository.findById(hotelId)).thenReturn(Optional.empty());

      // when
      this.mockMvc.perform(get("/hostels/{id}", hotelId))
          // then
          .andExpect(status().isNotFound());

      Mockito.verify(this.repository, Mockito.times(1)).findById(hotelId);
  }

  // Doit retourner une erreur d'autorisation (401) si l'utilisateur n'est pas connecté.
  @Test
  void shouldReturnUnauthorizedWhenNotLoggedIn() throws Exception {
      String hotelId = "1";
      // when
      this.mockMvc.perform(get("/hostels/{id}", hotelId))
          // then
          .andExpect(status().isUnauthorized());

      Mockito.verify(this.repository, Mockito.never()).findById(hotelId);
  }

  /*
  * POST /hostels
  */

  // Doit permettre à un utilisateur avec le rôle ADMIN d'ajouter un hôtel.
  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldAllowAdminToAddHotel() throws Exception {
      // given
      HostelRequest hostelRequest = new HostelRequest();
      hostelRequest.setName("Luxury Inn");
      hostelRequest.setAddress("123 Luxury St");
      hostelRequest.setCity("Paris");
      hostelRequest.setCountry("France");
      hostelRequest.setStars(5);
      hostelRequest.setRooms(100);
      hostelRequest.setAvailableRooms(50);
      hostelRequest.setPrice(200);

      Hotel savedHotel = new Hotel();
      savedHotel.setId("1");
      savedHotel.setName("Luxury Inn");
      savedHotel.setAddress("123 Luxury St");
      savedHotel.setCity("Paris");
      savedHotel.setCountry("France");
      savedHotel.setStars(5);
      savedHotel.setRooms(100);
      savedHotel.setAvailableRooms(50);
      savedHotel.setPrice(200);

      Mockito.when(this.repository.save(Mockito.any(Hotel.class))).thenReturn(savedHotel);

      // when
      this.mockMvc.perform(post("/hostels")
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(hostelRequest)))
          // then
          .andExpect(status().isCreated())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.name", Matchers.is("Luxury Inn")))
          .andExpect(jsonPath("$.address", Matchers.is("123 Luxury St")))
          .andExpect(jsonPath("$.city", Matchers.is("Paris")))
          .andExpect(jsonPath("$.country", Matchers.is("France")))
          .andExpect(jsonPath("$.stars", Matchers.is(5)));

      Mockito.verify(this.repository, Mockito.times(1)).save(Mockito.any(Hotel.class));
  }

  // Doit retourner une erreur d'autorisation (403) si l'utilisateur n'a pas le rôle ADMIN.
  @Test
  @WithMockUser(roles = "USER")
  void shouldDenyNonAdminUserToAddHotel() throws Exception {
      // given
      HostelRequest hostelRequest = new HostelRequest();
      hostelRequest.setName("Luxury Inn");

      // when
      this.mockMvc.perform(post("/hostels")
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(hostelRequest)))
          // then
          .andExpect(status().isForbidden());

      Mockito.verify(this.repository, Mockito.never()).save(Mockito.any(Hotel.class));
  }

  // Doit retourner une erreur 400 si les données de l'hôtel sont invalides.
  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldReturnBadRequestWhenAddingHotelWithInvalidData() throws Exception {
      // given
      HostelRequest hostelRequest = new HostelRequest();
      
      // when
      this.mockMvc.perform(post("/hostels")
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(hostelRequest)))
          // then
          .andExpect(status().isBadRequest()); // Vérifie que la réponse est 400 Bad Request

      Mockito.verify(this.repository, Mockito.never()).save(Mockito.any(Hotel.class));
  }

/*
 * PUT /hostels/{id}
 */

// Doit permettre à un utilisateur avec le rôle ADMIN de mettre à jour un hôtel existant.
@Test
@WithMockUser(roles = "ADMIN")
void shouldAllowAdminToUpdateHotel() throws Exception {
    // given
    String hotelId = "1";
    HostelRequest hostelRequest = new HostelRequest();
    hostelRequest.setName("Updated Luxury Inn");
    hostelRequest.setAddress("456 Updated St");
    hostelRequest.setCity("Paris");
    hostelRequest.setCountry("France");
    hostelRequest.setStars(5);
    hostelRequest.setRooms(100);
    hostelRequest.setAvailableRooms(50);
    hostelRequest.setPrice(200);

    Hotel existingHotel = new Hotel();
    existingHotel.setId(hotelId);
    existingHotel.setName("Luxury Inn");
    existingHotel.setAddress("123 Luxury St");
    existingHotel.setCity("Paris");
    existingHotel.setCountry("France");
    existingHotel.setStars(5);
    existingHotel.setRooms(100);
    existingHotel.setAvailableRooms(50);
    existingHotel.setPrice(200);

    Hotel updatedHotel = new Hotel();
    updatedHotel.setId(hotelId);
    updatedHotel.setName("Updated Luxury Inn");
    updatedHotel.setAddress("456 Updated St");
    updatedHotel.setCity("Paris");
    updatedHotel.setCountry("France");
    updatedHotel.setStars(5);
    updatedHotel.setRooms(100);
    updatedHotel.setAvailableRooms(50);
    updatedHotel.setPrice(200);

    Mockito.when(this.repository.findById(hotelId)).thenReturn(Optional.of(existingHotel));
    Mockito.when(this.repository.save(Mockito.any(Hotel.class))).thenReturn(updatedHotel);

    // when
    this.mockMvc.perform(put("/hostels/{id}", hotelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(hostelRequest)))
        // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", Matchers.is("Updated Luxury Inn")))
        .andExpect(jsonPath("$.address", Matchers.is("456 Updated St")))
        .andExpect(jsonPath("$.city", Matchers.is("Paris")))
        .andExpect(jsonPath("$.country", Matchers.is("France")))
        .andExpect(jsonPath("$.stars", Matchers.is(5)));

    Mockito.verify(this.repository, Mockito.times(1)).save(Mockito.any(Hotel.class));
}

    // Doit retourner une erreur 404 si l'hôtel n'existe pas.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundWhenUpdatingNonExistingHotel() throws Exception {
        // given
        String hotelId = "999"; // Un ID d'hôtel qui n'existe pas
        HostelRequest hostelRequest = new HostelRequest();
        hostelRequest.setName("Luxury Inn");

        Mockito.when(this.repository.findById(hotelId)).thenReturn(Optional.empty());

        // when
        this.mockMvc.perform(put("/hostels/{id}", hotelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(hostelRequest)))
            // then
            .andExpect(status().isNotFound());

        Mockito.verify(this.repository, Mockito.never()).save(Mockito.any(Hotel.class));
    }

    // Todo : Doit retourner une erreur 400 si la requete est invalide.

    // Doit retourner une erreur d'autorisation (403) si l'utilisateur n'a pas le rôle ADMIN.
    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyNonAdminUserToUpdateHotel() throws Exception {
        // given
        String hotelId = "1";
        HostelRequest hostelRequest = new HostelRequest();
        hostelRequest.setName("Luxury Inn");

        // when
        this.mockMvc.perform(put("/hostels/{id}", hotelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(hostelRequest)))
            // then
            .andExpect(status().isForbidden());

        Mockito.verify(this.repository, Mockito.never()).save(Mockito.any(Hotel.class));
    }

    /*
    * DELETE /hostels/{id}
    */

    // Doit permettre à un utilisateur avec le rôle ADMIN de supprimer un hôtel existant.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminToDeleteHotel() throws Exception {
        // given
        String hotelId = "1";
        Mockito.when(this.repository.existsById(hotelId)).thenReturn(true);

        // when
        this.mockMvc.perform(delete("/hostels/{id}", hotelId))
            // then
            .andExpect(status().isNoContent()); // Vérifie que la réponse est 204 No Content

        Mockito.verify(this.repository, Mockito.times(1)).deleteById(hotelId);
    }

    // Doit retourner une erreur 404 si l'hôtel n'existe pas.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundWhenDeletingNonExistingHotel() throws Exception {
        // given
        String hotelId = "999"; // Un ID d'hôtel qui n'existe pas
        Mockito.when(this.repository.existsById(hotelId)).thenReturn(false);

        // when
        this.mockMvc.perform(delete("/hostels/{id}", hotelId))
            // then
            .andExpect(status().isNotFound()); // Vérifie que la réponse est 404 Not Found

        Mockito.verify(this.repository, Mockito.never()).deleteById(hotelId);
    }

    // Doit retourner une erreur d'autorisation (403) si l'utilisateur n'a pas le rôle ADMIN.
    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyNonAdminUserToDeleteHotel() throws Exception {
        // given
        String hotelId = "1";
        
        // when
        this.mockMvc.perform(delete("/hostels/{id}", hotelId))
            // then
            .andExpect(status().isForbidden()); // Vérifie que la réponse est 403 Forbidden

        Mockito.verify(this.repository, Mockito.never()).deleteById(hotelId);
    }
}
