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

import fr.tpreservation.model.Car;
import fr.tpreservation.repo.CarRepository;
import fr.tpreservation.request.CarRequest;
import fr.tpreservation.response.CarResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarRepository carRepository;

    @GetMapping
    public List<CarResponse> findAll() {
        return this.carRepository.findAll()
            .stream()
            .map(this::convert)
            .toList();
    }

    
    @GetMapping("/availability/{isAvailable}")
    public List<CarResponse> findAvailableCars(@PathVariable Boolean isAvailable) {
        return this.carRepository.findByAvailable(isAvailable)
        .stream()
        .map(this::convert)
        .toList();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable String id) {
        Optional<Car> carOptional = carRepository.findById(id);
        if (carOptional.isPresent()) {
            CarResponse carResponse = convert(carOptional.get());
            return ResponseEntity.ok(carResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<CarResponse> addCar(@RequestBody CarRequest carRequest) {
        Car car = new Car();
        BeanUtils.copyProperties(carRequest, car);
        Car savedCar = carRepository.save(car);
        CarResponse carResponse = convert(savedCar);
        return ResponseEntity.status(201).body(carResponse);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable String id, @RequestBody CarRequest carRequest) {
        Optional<Car> carOptional = carRepository.findById(id);
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            BeanUtils.copyProperties(carRequest, car);
            Car updatedCar = carRepository.save(car);
            CarResponse carResponse = convert(updatedCar);
            return ResponseEntity.ok(carResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable String id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private CarResponse convert(Car car) {
        CarResponse resp = CarResponse.builder().build();
        BeanUtils.copyProperties(car, resp);
        return resp;
    }
}
