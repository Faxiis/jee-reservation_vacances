package fr.tpreservation.api;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import fr.tpreservation.config.JwtUtil;
import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.UserRepository;
import fr.tpreservation.request.SubscribeRequest;
import fr.tpreservation.services.UtilisateurService;

@SpringBootTest
@AutoConfigureMockMvc
public class UtilisateurControllerMockitoTest {
  @Autowired  
  private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilisateurService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

  /*
  * POST users/auth/
  */

  @Test
  @WithMockUser
  void shouldAuthenticateUserWithValidCredentials() throws Exception {
      String username = "user";
      String password = "password";
      List<String> roles = Arrays.asList("ROLE_USER");

      when(utilisateurService.getRolesByUsername(username)).thenReturn(roles);
      
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authToken);

      String expectedJwt = JwtUtil.generate(username, roles);
      
      mockMvc.perform(post("/users/auth")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
              .andExpect(status().isOk())
              .andExpect(content().string(expectedJwt));
  }

  @Test
  @WithMockUser
  void shouldNotAuthenticateUserWithBadCredentials() throws Exception {

    String username = "user";
      String password = "wrongpassword";

      // Simuler le comportement d'authentification pour renvoyer une exception
      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
          .thenThrow(new BadCredentialsException("Bad credentials"));

      mockMvc.perform(post("/users/auth")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
              .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void shouldAuthenticateUserAndGetRoles() throws Exception {
      // Arrange
      String username = "user";
      String password = "password";
      List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
  
      // Simuler le comportement pour renvoyer les rôles de l'utilisateur
      when(utilisateurService.getRolesByUsername(username)).thenReturn(roles);
  
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authToken);
  
      mockMvc.perform(post("/users/auth")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
              .andExpect(status().isOk());
  }

  /*
  * POST users/subscribe
  */

  @Test
  @WithMockUser
  void shouldReturnIdAccountWhenSubscribeSuccessful() throws Exception {
      // Arrange
      String username = "newUser";
      String password = "newPassword";
      String expectedId = "12345"; // ID simulé
  
      SubscribeRequest request = new SubscribeRequest();
      request.setUsername(username);
      request.setPassword(password);
  
      Utilisateur utilisateur = new Utilisateur();
      utilisateur.setId(expectedId); // Simule l'ID généré
      utilisateur.setUsername(username);
      utilisateur.setPassword(password); // Assure-toi que c'est après l'encodage
  
      // Mock du repository
      when(userRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> {
          Utilisateur savedUser = invocation.getArgument(0);
          savedUser.setId(expectedId); // Assigne l'ID simulé à l'utilisateur sauvegardé
          return savedUser;
      });
  
      mockMvc.perform(post("/users/subscribe")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
              .andExpect(status().isCreated())
              .andExpect(content().string(expectedId));
  }

  @Test
  void shouldThrowExceptionWhenRequestIsNull() throws Exception {
    mockMvc.perform(post("/users/subscribe")
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andExpect(status().isBadRequest());
  }

  @Test
  void shouldThrowExceptionWhenUsernameIsEmpty() throws Exception {
      // Arrange
      String password = "somePassword";
  
      // Créer une requête avec un nom d'utilisateur vide
      String requestContent = "{\"username\":\"\",\"password\":\"" + password + "\"}";
  
      mockMvc.perform(post("/users/subscribe")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestContent))
              .andExpect(status().isBadRequest());
  }
  
  @Test
  @WithMockUser
  void shouldThrowExceptionWhenPasswordIsEmpty() throws Exception {
          String username =  "BestUsernameEver";          
          String password = "";
  
          // Créer une requête avec un nom d'utilisateur vide
          String requestContent = "{\"username\":\"" + username +"\",\"password\":\"" + password + "\"}";
      
          mockMvc.perform(post("/users/subscribe")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestContent))
                  .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  void shouldThrowExceptionWhenUsernameAlreadyExists() throws Exception {
      // Arrange
      String existingUsername = "existingUser";
      String password = "password123";
  
      // Mock the behavior of the repository to indicate that the username already exists
      when(userRepository.existsByUsername(existingUsername)).thenReturn(true);
  
      // Create a request with the existing username
      String requestContent = "{\"username\":\"" + existingUsername + "\",\"password\":\"" + password + "\"}";
  
      mockMvc.perform(post("/users/subscribe")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestContent))
              .andExpect(status().isConflict());
  }

  /*
  * GET users/{id}
  */
  @Test
  @WithMockUser
  void shouldReturnUserDetailById() throws Exception {
      // Arrange
      String userId = "12345";
      Utilisateur utilisateur = new Utilisateur();
      utilisateur.setId(userId);
      utilisateur.setUsername("testUser");
      utilisateur.setReservations(Arrays.asList()); // Aucune réservation pour cet exemple
  
      // Simuler le comportement de la méthode findById
      when(userRepository.findById(userId)).thenReturn(Optional.of(utilisateur));


      mockMvc.perform(get("/users/" + userId)
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(content().json("{\"id\":\"" + userId + "\",\"username\":\"testUser\",\"reservations\":[]}"));
  }
  
  @Test
  @WithMockUser
  void shouldThrowUserNotFoundExceptionWhenUserNotFound() throws Exception {
      // Arrange
      String userId = "nonExistentId";
  
      // Simuler le comportement de la méthode findById pour renvoyer un Optional vide
      when(userRepository.findById(userId)).thenReturn(Optional.empty());
  
      mockMvc.perform(get("/users/" + userId)
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isNotFound());
  }

  /**
   *  PUT /users/{id}
   */

  @Test
  @WithMockUser
  void shouldUpdateUserWhenValidRequest() throws Exception {
      // Arrange
      String userId = "12345";
      String updatedUsername = "updatedUser";
      String updatedEmail = "updated@example.com";
      String updatedFirstname = "Updated";
      String updatedLastname = "User";

      // Créer l'utilisateur d'origine
      Utilisateur existingUser = new Utilisateur();
      existingUser.setId(userId);
      existingUser.setUsername("originalUser");
      existingUser.setEmail("original@example.com");
      existingUser.setFirstName("Original");
      existingUser.setLastName("User");

      // Simuler le comportement de la méthode findById
      when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

      // Créer la requête de mise à jour
      SubscribeRequest updateRequest = new SubscribeRequest();
      updateRequest.setUsername(updatedUsername);
      updateRequest.setEmail(updatedEmail);
      updateRequest.setFirstname(updatedFirstname);
      updateRequest.setLastname(updatedLastname);

      // Simuler le comportement de la méthode save pour vérifier que l'utilisateur est mis à jour
      when(userRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

      mockMvc.perform(put("/users/" + userId)
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"username\":\"" + updatedUsername + "\",\"email\":\"" + updatedEmail + "\",\"firstname\":\"" + updatedFirstname + "\",\"lastname\":\"" + updatedLastname + "\"}"))
              .andExpect(status().isOk());

      // Vérifier que l'utilisateur a bien été mis à jour
      verify(userRepository).save(existingUser);
      assertEquals(updatedUsername, existingUser.getUsername());
      assertEquals(updatedEmail, existingUser.getEmail());
      assertEquals(updatedFirstname, existingUser.getFirstName());
      assertEquals(updatedLastname, existingUser.getLastName());
  }

  @Test
  @WithMockUser
  void shouldThrowNotFoundExeptionWhenUserNotFound() throws Exception {
      // Arrange
      String userId = "nonExistentId";

      // Simuler le comportement de la méthode findById pour renvoyer un Optional vide
      when(userRepository.findById(userId)).thenReturn(Optional.empty());

      // Créer une requête de mise à jour avec des données fictives
      String requestContent = "{\"username\":\"updatedUser\",\"email\":\"updated@example.com\"}";

      mockMvc.perform(put("/users/" + userId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestContent))
              .andExpect(status().isNotFound());
  }

  /**
   * DELETE /users/{id}
   */

  @Test
  @WithMockUser
  void shouldReturnNoContentWhenUserExists() throws Exception {

      String userId = "12345";
      Utilisateur utilisateur = new Utilisateur();
      utilisateur.setId(userId);
    
      // Simuler le comportement de la méthode findById pour renvoyer l'utilisateur
      when(userRepository.findById(userId)).thenReturn(Optional.of(utilisateur));
    
      mockMvc.perform(delete("/users/" + userId)
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isNoContent());
      
      // Vérifier que l'utilisateur a bien été supprimé du repository
      verify(userRepository).delete(utilisateur);
  }

  @Test
  @WithMockUser
  void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() throws Exception {
      String userId = "nonExistentId";
    
      when(userRepository.findById(userId)).thenReturn(Optional.empty());
    
      mockMvc.perform(delete("/users/" + userId)
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isNotFound());
      
      // Vérifier que la méthode delete n'a pas été appelée
      verify(userRepository, never()).delete(any(Utilisateur.class));
  }
}
