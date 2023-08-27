package com.github.bzalyaliev.server.controller;

import com.github.bzalyaliev.regulator.Regulator;
import com.github.bzalyaliev.regulator.RegulatorImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regulator")

public class RegulatorController {

    private final Regulator regulator = RegulatorImpl.getInstance();

    @PostMapping("/set")
    public ResponseEntity<String> setTemperature(@RequestBody double temperature) {
        regulator.setTemperature(temperature);
        return ResponseEntity.ok("Temperature set successfully");
    }

    @GetMapping("/current")
    public ResponseEntity<Double> getLastTemperature() {
        List<Double> temperatures = regulator.getTemperatureValues();
        Double currentTemperature;
        if (temperatures.size() > 0) {
            currentTemperature = temperatures.get(temperatures.size() - 1);
            return ResponseEntity.ok(currentTemperature);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/last")
    public ResponseEntity<Double> getLastTemperatureValues(@RequestParam("offset") int offset,
                                                           @RequestParam("count") int count) {
        List<Double> temperatures = regulator.getTemperatureValues();
        int totalTemperatures = temperatures.size();

        if (totalTemperatures > 0 && offset >= 0 && count > 0) {
            int startIndex = Math.max(totalTemperatures - offset - count, 0);
            int endIndex = Math.min(totalTemperatures - offset, totalTemperatures);

            if (endIndex >= startIndex) {
                List<Double> selectedTemperatures = temperatures.subList(startIndex, endIndex);
                return ResponseEntity.ok(selectedTemperatures.get(selectedTemperatures.size() - 1));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearTemperatureHistory() {
        regulator.clearTemperatureValues();
        return ResponseEntity.ok("Temperature history cleared");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Double>> getAllTemperatures() {
        List<Double> temperatures = regulator.getTemperatureValues();
        if (temperatures.size() > 0) {
            return ResponseEntity.ok(temperatures);
        }
        return ResponseEntity.notFound().build();
    }

}
