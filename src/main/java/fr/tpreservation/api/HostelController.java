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

import fr.tpreservation.model.Hotel;
import fr.tpreservation.repo.HostelRepository;
import fr.tpreservation.request.HostelRequest;
import fr.tpreservation.response.HostelResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/hostels")
@RequiredArgsConstructor
public class HostelController {
      private final HostelRepository hostelRepository;

    @GetMapping
    public List<HostelResponse> findAll() {
        return this.hostelRepository.findAll()
            .stream()
            .map(this::convert)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HostelResponse> getHotelById(@PathVariable String id) {
        Optional<Hotel> hotelOptional = hostelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            HostelResponse hotelResponse = convert(hotelOptional.get());
            return ResponseEntity.ok(hotelResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<HostelResponse> addHotel(@RequestBody HostelRequest hostelRequest) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hostelRequest, hotel);
        Hotel savedHotel = hostelRepository.save(hotel);
        HostelResponse hotelResponse = convert(savedHotel);
        return ResponseEntity.status(201).body(hotelResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HostelResponse> updateHotel(@PathVariable String id, @RequestBody HostelRequest hostelRequest) {
        Optional<Hotel> hotelOptional = hostelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            Hotel hotel = hotelOptional.get();
            BeanUtils.copyProperties(hostelRequest, hotel, "id"); // Ne pas copier l'ID
            Hotel updatedHotel = hostelRepository.save(hotel);
            HostelResponse hotelResponse = convert(updatedHotel);
            return ResponseEntity.ok(hotelResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable String id) {
        if (hostelRepository.existsById(id)) {
            hostelRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private HostelResponse convert(Hotel hostel) {
      HostelResponse resp = HostelResponse.builder().build();
      BeanUtils.copyProperties(hostel, resp);
      return resp;
    }
}
