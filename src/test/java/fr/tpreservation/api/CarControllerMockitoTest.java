package fr.tpreservation.api;

import static org.mockito.ArgumentMatchers.anyBoolean;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tpreservation.model.Car;
import fr.tpreservation.repo.CarRepository;
import fr.tpreservation.request.CarRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarRepository repository;
    private static final CarRequest carRequest = new CarRequest() {{
        setModel("Model S");
        setBrand("Tesla");
        setCapacity(5);
        setPricePerDay(100);
        setLocation("Paris");
        setFuelType("Electric");
        setDescription("Luxurious electric car");
        setAvailable(true);
    }};
    
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

    /*
     * GET /cars/{id}
     */

    // Doit retourner une voiture par ID si elle existe.
    @Test
    @WithMockUser
    void shouldReturnCarByIdWhenExists() throws Exception {
        // given
        String carId = "1";
        Car car = new Car();
        car.setId(carId);
        car.setModel("Model S");
        car.setBrand("Tesla");
        car.setCapacity(5);
        car.setPricePerDay(100);
        car.setLocation("Paris");
        car.setFuelType("Electric");
        car.setDescription("Luxurious electric car");
        car.setAvailable(true);

        Mockito.when(this.repository.findById(carId)).thenReturn(Optional.of(car));

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/{id}", carId))

        // then
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.model", Matchers.is("Model S")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.brand", Matchers.is("Tesla")));

        Mockito.verify(this.repository, Mockito.times(1)).findById(carId);
    }

    // Doit retourner une erreur 404 si la voiture n'existe pas.
    @Test
    @WithMockUser()
    void shouldReturnNotFoundWhenCarDoesNotExist() throws Exception {
        // given
        String carId = "1";
        Mockito.when(this.repository.findById(carId)).thenReturn(Optional.empty());

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/{id}", carId))

        // then
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(this.repository, Mockito.times(1)).findById(carId);
    }

    // Doit retourner une erreur d'autorisation (401) si l'utilisateur n'est pas connecté.
    @Test
    void shouldReturnUnauthorizedWhenNotLoggedForCarById() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cars/{id}", "1"))

        // then
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(this.repository, Mockito.never()).findById(Mockito.anyString());
    }

    /*
     * POST /cars
     */

    // Doit ajouter une nouvelle voiture et retourner un statut 201 avec la voiture créée si l'utilisateur a le rôle ADMIN.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddCarWhenAdmin() throws Exception {
        // given

        Car car = new Car();
        car.setId("1");
        car.setModel("Model S");
        car.setBrand("Tesla");
        car.setCapacity(5);
        car.setPricePerDay(100);
        car.setLocation("Paris");
        car.setFuelType("Electric");
        car.setDescription("Luxurious electric car");
        car.setAvailable(true);

        Mockito.when(this.repository.save(Mockito.any(Car.class))).thenReturn(car);

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(carRequest)))

        // then
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.model", Matchers.is("Model S")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.brand", Matchers.is("Tesla")));

        Mockito.verify(this.repository, Mockito.times(1)).save(Mockito.any(Car.class));
    }

    // Doit retourner une erreur 403 si l'utilisateur n'a pas le rôle ADMIN.
    @Test
    @WithMockUser()  // Utilisateur sans rôle ADMIN
    void shouldReturnForbiddenWhenNotAdmin() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(carRequest)))

        // then
            .andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(this.repository, Mockito.never()).save(Mockito.any(Car.class));
    }

    // Doit retourner une erreur d'autorisation (401) si l'utilisateur n'est pas connecté.
    @Test
    void shouldReturnUnauthorizedWhenNotLoggedToAddCar() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(carRequest)))

        // then
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(this.repository, Mockito.never()).save(Mockito.any(Car.class));
    }
    
    // Doit retourner une erreur 400 si la requête pour ajouter une voiture est invalide.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequestWhenCarRequestIsInvalid() throws Exception {
        // given
        CarRequest invalidCarRequest = new CarRequest();
        
        Car car = new Car();
        car.setId("1");
        car.setModel("Model S");
        car.setBrand("Tesla");
        car.setCapacity(5);
        car.setPricePerDay(100);
        car.setLocation("Paris");
        car.setFuelType("Electric");
        car.setDescription("Luxurious electric car");
        car.setAvailable(true);

        Mockito.when(this.repository.save(Mockito.any(Car.class))).thenReturn(car);

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidCarRequest)))

        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /*
    * PUT /cars/{id}
    */

    // Doit mettre à jour une voiture et retourner un statut 200 si l'utilisateur a le rôle ADMIN.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCarWhenAdmin() throws Exception {
        // given
        String carId = "1";
        Car existingCar = new Car();
        existingCar.setId(carId);
        existingCar.setModel("Model S");
        existingCar.setBrand("Tesla");
        
        carRequest.setModel("Model 3");
        
        Mockito.when(this.repository.findById(carId)).thenReturn(Optional.of(existingCar));
        Mockito.when(this.repository.save(Mockito.any(Car.class))).thenReturn(existingCar);

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.put("/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(carRequest)))

        // then
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.model", Matchers.is("Model 3")));

        Mockito.verify(this.repository, Mockito.times(1)).findById(carId);
        Mockito.verify(this.repository, Mockito.times(1)).save(Mockito.any(Car.class));
    }

    // Doit retourner une erreur 404 si la voiture n'existe pas.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundWhenUpdatingNonExistentCar() throws Exception {
        // given
        String carId = "1";
        CarRequest carRequest = new CarRequest();
        
        Mockito.when(this.repository.findById(carId)).thenReturn(Optional.empty());

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.put("/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(carRequest)))

        // then
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(this.repository, Mockito.times(1)).findById(carId);
    }

    // Doit retourner une erreur 403 si l'utilisateur n'a pas le rôle ADMIN.
    @Test
    @WithMockUser // Utilisateur sans rôle ADMIN
    void shouldReturnForbiddenWhenNotAdminOnUpdate() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.put("/cars/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new CarRequest())))

        // then
            .andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(this.repository, Mockito.never()).findById(Mockito.anyString());
    }

    // Doit retourner une erreur d'autorisation (401) si l'utilisateur n'est pas connecté.
    @Test
    void shouldReturnUnauthorizedWhenNotLoggedForUpdateCar() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.put("/cars/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new CarRequest())))

        // then
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(this.repository, Mockito.never()).findById(Mockito.anyString());
    }

    // Todo : Doit retourner une erreur 400 si la requete est invalide.


    /*
    * DELETE /cars/{id}
    */

    // Doit supprimer une voiture et retourner un statut 204 si l'utilisateur a le rôle ADMIN.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCarWhenAdmin() throws Exception {
        // given
        String carId = "1";
        Mockito.when(this.repository.existsById(carId)).thenReturn(true);

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", carId))

        // then
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(this.repository, Mockito.times(1)).deleteById(carId);
    }

    // Doit retourner une erreur 404 si la voiture n'existe pas.
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundWhenDeletingNonExistentCar() throws Exception {
        // given
        String carId = "1";
        Mockito.when(this.repository.existsById(carId)).thenReturn(false);

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", carId))

        // then
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(this.repository, Mockito.never()).deleteById(carId);
    }

    // Doit retourner une erreur 403 si l'utilisateur n'a pas le rôle ADMIN.
    @Test
    @WithMockUser // Utilisateur sans rôle ADMIN
    void shouldReturnForbiddenWhenNotAdminOnDelete() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", "1"))

        // then
            .andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(this.repository, Mockito.never()).deleteById(Mockito.anyString());
    }

    // Doit retourner une erreur d'autorisation (401) si l'utilisateur n'est pas connecté.
    @Test
    void shouldReturnUnauthorizedWhenNotLoggedForDeleteCar() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", "1"))

        // then
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(this.repository, Mockito.never()).deleteById(Mockito.anyString());
    }

}
