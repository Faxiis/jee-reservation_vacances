package fr.tpreservation.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tpreservation.model.Flight;
import fr.tpreservation.repo.FlightRepository;
import fr.tpreservation.request.FlightRequest;
import fr.tpreservation.response.FlightResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightApiController {
    private final FlightRepository flightRepository;

    @GetMapping
    public List<FlightResponse> findAll() {
        return this.flightRepository.findAll()
            .stream()
            .map(this::convert)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable String id) {
        Optional<Flight> flightOptional = flightRepository.findById(id);
        if (flightOptional.isPresent()) {
            FlightResponse flightResponse = convert(flightOptional.get());
            return ResponseEntity.ok(flightResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@Valid @RequestBody FlightRequest flightRequest) {
        Flight flight = new Flight();
        BeanUtils.copyProperties(flightRequest, flight);
        Flight savedFlight = flightRepository.save(flight);
        FlightResponse flightResponse = convert(savedFlight);
        return ResponseEntity.status(201).body(flightResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> updateFlight(@PathVariable String id, @RequestBody FlightRequest flightRequest) {
        Optional<Flight> flightOptional = flightRepository.findById(id);
        if (flightOptional.isPresent()) {
            Flight flight = flightOptional.get();
            BeanUtils.copyProperties(flightRequest, flight, "id");
            Flight updateFlight = flightRepository.save(flight);
            FlightResponse flightResponse = convert(updateFlight);
            return ResponseEntity.ok(flightResponse);
        } else {
            return ResponseEntity.notFound().build();
    }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable String id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    private FlightResponse convert(Flight flight) {
      FlightResponse resp = FlightResponse.builder().build();
      BeanUtils.copyProperties(flight, resp);
      return resp;
    }
}