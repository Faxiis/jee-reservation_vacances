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

import fr.tpreservation.model.ParkingPlace;
import fr.tpreservation.repo.ParkingRepository;
import fr.tpreservation.request.ParkingRequest;
import fr.tpreservation.response.ParkingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parkings")
@RequiredArgsConstructor
public class ParkingController {
    private final ParkingRepository parkingRepository;

    @GetMapping
    public List<ParkingResponse> findAll() {
        return this.parkingRepository.findByIsAvailableTrue()
            .stream()
            .map(this::convert)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingResponse> getParkingPlaceById(@PathVariable String id) {
        Optional<ParkingPlace> parkingPlaceOptional = parkingRepository.findById(id);
        if (parkingPlaceOptional.isPresent()) {
            ParkingResponse parkingResponse = convert(parkingPlaceOptional.get());
            return ResponseEntity.ok(parkingResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ParkingResponse> createParkingPlace(@Valid @RequestBody ParkingRequest parkingRequest) {
        ParkingPlace parking = new ParkingPlace();
        BeanUtils.copyProperties(parkingRequest, parking);
        ParkingPlace savedParking = parkingRepository.save(parking);
        ParkingResponse parkingResponse = convert(savedParking);
        return ResponseEntity.status(201).body(parkingResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingResponse> updateParkingPlace(@PathVariable String id, @RequestBody ParkingRequest parkingRequest) {
        Optional<ParkingPlace> parkingPlaceOptional = parkingRepository.findById(id);
        if (parkingPlaceOptional.isPresent()) {
            ParkingPlace parking = parkingPlaceOptional.get();
            BeanUtils.copyProperties(parkingRequest, parking, "id");
            ParkingPlace updateParking = parkingRepository.save(parking);
            ParkingResponse parkingResponse = convert(updateParking);
            return ResponseEntity.ok(parkingResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingPlace(@PathVariable String id) {
        if (parkingRepository.existsById(id)) {
            parkingRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ParkingResponse convert(ParkingPlace parking) {
        ParkingResponse resp = ParkingResponse.builder().build();
        BeanUtils.copyProperties(parking, resp);
        return resp;
    }
}
