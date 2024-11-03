package fr.tpreservation.api;

import fr.tpreservation.model.FlightReservation;
import fr.tpreservation.repo.FlightReservationRepository;
import fr.tpreservation.request.FlightReservation.PostFlightReservationRequest;
import fr.tpreservation.request.FlightReservation.PutFlightReservationRequest;
import fr.tpreservation.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flight-reservation")
@RequiredArgsConstructor
public class FlightReservationController {
    private final FlightReservationRepository flightReservationRepository;

    @GetMapping
    public List<ReservationResponse> findAll() {
        return flightReservationRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getFlightReservationById(@PathVariable String id) {
        Optional<FlightReservation> flightReservationOptional = flightReservationRepository.findById(id);
        if (flightReservationOptional.isPresent()) {
            ReservationResponse flightReservationResponse = convert(flightReservationOptional.get());
            return ResponseEntity.ok(flightReservationResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addFlightReservation(@Valid @RequestBody PostFlightReservationRequest flightReservationRequest) {
        FlightReservation flightReservation = new FlightReservation();
        BeanUtils.copyProperties(flightReservationRequest, flightReservation);
        FlightReservation savedFlightReservation = flightReservationRepository.save(flightReservation);
        ReservationResponse flightReservationResponse = convert(savedFlightReservation);
        return ResponseEntity.status(201).body(flightReservationResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateFlightReservation(@PathVariable String id, @Valid @RequestBody PutFlightReservationRequest flightReservationRequest) {
        Optional<FlightReservation> flightReservationOptional = flightReservationRepository.findById(id);
        if (flightReservationOptional.isPresent()) {
            FlightReservation flightReservation = flightReservationOptional.get();
            BeanUtils.copyProperties(flightReservationRequest, flightReservation, "id");
            FlightReservation updatedFlightReservation = flightReservationRepository.save(flightReservation);
            ReservationResponse flightReservationResponse = convert(updatedFlightReservation);
            return ResponseEntity.ok(flightReservationResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlightReservation(@PathVariable String id) {
        if (flightReservationRepository.existsById(id)) {
            flightReservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ReservationResponse convert(FlightReservation flightReservation) {
        ReservationResponse resp = ReservationResponse.builder().build();
        BeanUtils.copyProperties(flightReservation, resp);
        return resp;
    }
}