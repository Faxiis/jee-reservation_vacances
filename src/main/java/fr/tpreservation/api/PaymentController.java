package fr.tpreservation.api;

import fr.tpreservation.model.Payment;
import fr.tpreservation.repo.PaymentRepository;
import fr.tpreservation.request.Payment.PaymentRequest;
import fr.tpreservation.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentRepository paymentRepository;

    @PostMapping
    public ResponseEntity<PaymentResponse> addPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentRequest, payment);
        payment.setStatus("PENDING");
        Payment savedPayment = paymentRepository.save(payment);
        PaymentResponse paymentResponse = convert(savedPayment);
        return ResponseEntity.status(201).body(paymentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        if (paymentOptional.isPresent()) {
            PaymentResponse paymentResponse = convert(paymentOptional.get());
            return ResponseEntity.ok(paymentResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private PaymentResponse convert(Payment payment) {
        PaymentResponse resp = PaymentResponse.builder().build();
        BeanUtils.copyProperties(payment, resp);
        return resp;
    }
}