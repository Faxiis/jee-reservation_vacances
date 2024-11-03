package fr.tpreservation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.tpreservation.model.Hotel;
import fr.tpreservation.model.HotelReservation;
import fr.tpreservation.model.Payment;
import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.PaymentRepository;
import fr.tpreservation.request.Payment.PaymentRequest;
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
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private void setupObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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

    private Hotel createHotel() {
        Hotel hotel = new Hotel();
        hotel.setId("1");
        hotel.setName("Hostel One");
        hotel.setAddress("Paris");
        return hotel;
    }

    private HotelReservation createHotelReservation(Utilisateur user) {
        HotelReservation reservation = new HotelReservation();
        reservation.setId("1");
        reservation.setUser(user);
        reservation.setReservationDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        return reservation;
    }

    private Payment createPayment() {
        Payment payment = new Payment();
        payment.setId("1");
        payment.setReservationId("1");
        payment.setAmount(100.0);
        payment.setPaymentDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        payment.setStatus("PENDING");
        return payment;
    }

    @Test
    @WithMockUser
    void shouldGetPaymentById() throws Exception {
        setupObjectMapper();
        Payment payment = createPayment();

        Mockito.when(repository.findById("1")).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.amount", Matchers.is(100.0)))
                .andExpect(jsonPath("$.paymentDate", Matchers.is("2023-10-10T10:00:00")))
                .andExpect(jsonPath("$.status", Matchers.is("PENDING")));

        Mockito.verify(repository, Mockito.times(1)).findById("1");
    }


    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenPaymentByIdDoesNotExist() throws Exception {
        Mockito.when(repository.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/payments/999"))
                .andExpect(status().isNotFound());

        Mockito.verify(repository, Mockito.times(1)).findById("999");
    }


    @Test
    void shouldReturnUnauthorizedWhenNotLogged() throws Exception {
        mockMvc.perform(get("/payments"))
                .andExpect(status().isUnauthorized());

        Mockito.verify(repository, Mockito.never()).findAll();
    }

    @Test
    @WithMockUser
    void shouldAddPayment() throws Exception {
        setupObjectMapper();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setReservationId("1");
        paymentRequest.setAmount(100.0);
        paymentRequest.setPaymentMethod("CREDIT_CARD");
        paymentRequest.setPaymentDate(LocalDateTime.of(2023, 10, 10, 10, 0));
        Payment payment = createPayment();

        Mockito.when(repository.save(Mockito.any(Payment.class))).thenReturn(payment);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.amount", Matchers.is(100.0)))
                .andExpect(jsonPath("$.paymentDate", Matchers.is("2023-10-10T10:00:00")))
                .andExpect(jsonPath("$.status", Matchers.is("PENDING")));

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Payment.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenCreatingPaymentWithMissingFields() throws Exception {
        setupObjectMapper();
        PaymentRequest paymentRequest = new PaymentRequest();

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLoggedForPost() throws Exception {
        setupObjectMapper();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setReservationId("1");
        paymentRequest.setAmount(100.0);
        paymentRequest.setPaymentMethod("CREDIT_CARD");
        paymentRequest.setPaymentDate(LocalDateTime.of(2023, 10, 10, 10, 0));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isUnauthorized());
    }
}
