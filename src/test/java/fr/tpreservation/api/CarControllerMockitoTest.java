package fr.tpreservation.api;

import static org.mockito.ArgumentMatchers.anyBoolean;

import java.util.List;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import fr.tpreservation.model.Car;
import fr.tpreservation.repo.CarRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarRepository repository;

    /*
     * GET /cars
     */

    // Doit retourner une liste de voiture si l'utilisateur est connecté.
    @Test
    @WithMockUser
    void shouldFindAllCarsWhenLogged() throws Exception {
        // given
        Car car1 = new Car();
        car1.setId("1"); 
        car1.setModel("Model S");
        car1.setBrand("Tesla");
        car1.setCapacity(5);
        car1.setPricePerDay(100);
        car1.setLocation("Paris");
        car1.setFuelType("Electric");
        car1.setDescription("Luxurious electric car");
        car1.setAvailable(true);
    
        List<Car> cars = List.of(car1);
    
        Mockito.when(this.repository.findAll()).thenReturn(cars);
    
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
            
        // then
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("Model S")));
    
        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    // Doit retourner une liste vide si aucune voiture n'existe.
    @Test
    @WithMockUser
    void shouldFindAllCarsReturnsEmptyList() throws Exception {
        // given
        Mockito.when(this.repository.findAll()).thenReturn(List.of());

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars"))

        // then
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(this.repository, Mockito.times(1)).findAll();
    }

    // Doit retourner une erreur d'autorisation (401) si l'utilisateur n'est pas connecté.
    @Test
    void shouldReturnUnauthorizedWhenNotLogged() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars"))

        // then
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        
        Mockito.verify(this.repository, Mockito.never()).findAll();
    }

    /*
     * GET /cars/{availability}
     */

    @Test
    @WithMockUser
    void shouldFindAvailableCarsWhenLogged() throws Exception {
        // given
        Car car1 = new Car();
        car1.setId("1");
        car1.setModel("Model S");
        car1.setBrand("Tesla");
        car1.setCapacity(5);
        car1.setPricePerDay(100);
        car1.setLocation("Paris");
        car1.setFuelType("Electric");
        car1.setDescription("Luxurious electric car");
        car1.setAvailable(true);

        List<Car> cars = List.of(car1);

        Mockito.when(this.repository.findByAvailable(true)).thenReturn(cars);

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/availability/{isAvailable}", true))

        // then
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("Model S")));

        Mockito.verify(this.repository, Mockito.times(1)).findByAvailable(true);
}

    // Doit retourner une liste vide si aucune voiture n'existe.
    @Test
    @WithMockUser
    void shouldFindAvailableCarsReturnsEmptyList() throws Exception {
        // given
        Mockito.when(this.repository.findByAvailable(true)).thenReturn(List.of());

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/availability/{isAvailable}", true))

        // then
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(this.repository, Mockito.times(1)).findByAvailable(true);
    }

    // Doit retourner une liste de voitures non disponibles si l'utilisateur est connecté.
    @Test
    @WithMockUser
    void shouldFindUnavailableCarsWhenLogged() throws Exception {
    // given
    Car car1 = new Car();
    car1.setId("1");
    car1.setModel("Model X");
    car1.setBrand("Tesla");
    car1.setCapacity(5);
    car1.setPricePerDay(150);
    car1.setLocation("Lyon");
    car1.setFuelType("Electric");
    car1.setDescription("Large electric SUV");
    car1.setAvailable(false);

    List<Car> cars = List.of(car1);

    Mockito.when(this.repository.findByAvailable(false)).thenReturn(cars);

    // when
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/availability/{isAvailable}", false))

    // then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("Model X")))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].available", Matchers.is(false)));

    Mockito.verify(this.repository, Mockito.times(1)).findByAvailable(false);
    }

    // Doit retourner une erreur d'autorisation (401) si l'utilisateur n'est pas connecté.
    @Test
    void shouldReturnUnauthorizedWhenNotLoggedForAvailability() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/availability/{isAvailable}", true))

        // then
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(this.repository, Mockito.never()).findByAvailable(anyBoolean());
    }
}
