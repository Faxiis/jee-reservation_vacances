package fr.tpreservation.api;

import fr.tpreservation.model.CarReservation;
import fr.tpreservation.repo.CarReservationRepository;
import fr.tpreservation.request.CarReservation.PostCarReservationRequest;
import fr.tpreservation.request.CarReservation.PutCarReservationRequest;
import fr.tpreservation.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/car-reservation")
@RequiredArgsConstructor
public class CarReservationController {
    private final CarReservationRepository carReservationRepository;

    @GetMapping
    public List<ReservationResponse> findAll() {
        return carReservationRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getCarReservationById(@PathVariable String id) {
        Optional<CarReservation> carReservationOptional = carReservationRepository.findById(id);
        if (carReservationOptional.isPresent()) {
            ReservationResponse carReservationResponse = convert(carReservationOptional.get());
            return ResponseEntity.ok(carReservationResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addCarReservation(@Valid @RequestBody PostCarReservationRequest carReservationRequest) {
        CarReservation carReservation = new CarReservation();
        carReservation.setReservationDate(LocalDateTime.now());
        BeanUtils.copyProperties(carReservationRequest, carReservation);
        CarReservation savedCarReservation = carReservationRepository.save(carReservation);
        ReservationResponse carReservationResponse = convert(savedCarReservation);
        return ResponseEntity.status(201).body(carReservationResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateCarReservation(@PathVariable String id, @Valid @RequestBody PutCarReservationRequest carReservationRequest) {
        Optional<CarReservation> carReservationOptional = carReservationRepository.findById(id);
        if (carReservationOptional.isPresent()) {
            CarReservation carReservation = carReservationOptional.get();
            BeanUtils.copyProperties(carReservationRequest, carReservation, "id");
            CarReservation updatedCarReservation = carReservationRepository.save(carReservation);
            ReservationResponse carReservationResponse = convert(updatedCarReservation);
            return ResponseEntity.ok(carReservationResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarReservation(@PathVariable String id) {
        if (carReservationRepository.existsById(id)) {
            carReservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //private ReservationResponse convert(CarReservation carReservation) {
    //    ReservationResponse resp = ReservationResponse.builder().build();
    //    BeanUtils.copyProperties(carReservation, resp);
    //    return resp;
    //}

    private ReservationResponse convert(CarReservation carReservation) {
        ReservationResponse resp = ReservationResponse.builder().build();
        resp.setId(carReservation.getId());
        resp.setReservationDate(carReservation.getReservationDate());
        resp.setUtilisateurId(carReservation.getUser().getId());
        return resp;
    }
}