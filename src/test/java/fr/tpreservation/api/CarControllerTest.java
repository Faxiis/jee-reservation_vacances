package fr.tpreservation.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import fr.tpreservation.repo.CarRepository;


@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {
      @Autowired
    private MockMvc mockMvc;

    
    @MockBean
    private CarRepository repository;

    // Doit retourner une exception 401 si l'utilisateur n'est pas connecté.
    @Test
    void shouldFindAllCarsUnauthorizedWhenNotLogged() throws Exception {
        // given

        // when
        this.mockMvc
            .perform(MockMvcRequestBuilders.get("/cars"))
        
        // then
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        ;
    }
}
