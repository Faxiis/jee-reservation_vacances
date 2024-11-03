package fr.tpreservation.api;

import fr.tpreservation.model.HotelReservation;
import fr.tpreservation.repo.HostelReservationRepository;
import fr.tpreservation.request.HostelReservation.PostHostelReservationRequest;
import fr.tpreservation.request.HostelReservation.PutHostelReservationRequest;
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
@RequestMapping("/hostel-reservation")
@RequiredArgsConstructor
public class HostelReservationController {
    private final HostelReservationRepository hostelReservationRepository;

    @GetMapping
    public List<ReservationResponse> findAll() {
        return hostelReservationRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getHostelReservationById(@PathVariable String id) {
        Optional<HotelReservation> hostelReservationOptional = hostelReservationRepository.findById(id);
        if (hostelReservationOptional.isPresent()) {
            ReservationResponse hostelReservationResponse = convert(hostelReservationOptional.get());
            return ResponseEntity.ok(hostelReservationResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addHostelReservation(@Valid @RequestBody PostHostelReservationRequest hostelReservationRequest) {
        HotelReservation hostelReservation = new HotelReservation();
        BeanUtils.copyProperties(hostelReservationRequest, hostelReservation);
        HotelReservation savedHostelReservation = hostelReservationRepository.save(hostelReservation);
        ReservationResponse hostelReservationResponse = convert(savedHostelReservation);
        return ResponseEntity.status(201).body(hostelReservationResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateHostelReservation(@PathVariable String id, @Valid @RequestBody PutHostelReservationRequest hostelReservationRequest) {
        Optional<HotelReservation> hostelReservationOptional = hostelReservationRepository.findById(id);
        if (hostelReservationOptional.isPresent()) {
            HotelReservation hostelReservation = hostelReservationOptional.get();
            BeanUtils.copyProperties(hostelReservationRequest, hostelReservation, "id");
            HotelReservation updatedHostelReservation = hostelReservationRepository.save(hostelReservation);
            ReservationResponse hostelReservationResponse = convert(updatedHostelReservation);
            return ResponseEntity.ok(hostelReservationResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHostelReservation(@PathVariable String id) {
        if (hostelReservationRepository.existsById(id)) {
            hostelReservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ReservationResponse convert(HotelReservation hostelReservation) {
        ReservationResponse resp = ReservationResponse.builder().build();
        BeanUtils.copyProperties(hostelReservation, resp);
        return resp;
    }
}