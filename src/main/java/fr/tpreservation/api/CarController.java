package fr.tpreservation.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tpreservation.model.Car;
import fr.tpreservation.repo.CarRepository;
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
            .toList()
        ;
    }

        private CarResponse convert(Car car) {
          CarResponse resp = CarResponse.builder().build();

        BeanUtils.copyProperties(car, resp);

        return resp;
    }
}
