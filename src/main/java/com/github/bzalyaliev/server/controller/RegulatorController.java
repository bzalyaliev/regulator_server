package com.github.bzalyaliev.server.controller;

import com.github.bzalyaliev.regulator.Regulator;
import com.github.bzalyaliev.regulator.RegulatorImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regulator")

public class RegulatorController {

    private final Regulator regulator = RegulatorImpl.getInstance();

    private HttpHeaders createTestHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("test", "1");
        return headers;
    }

    @PostMapping("/set")
    public ResponseEntity<String> setTemperature(@RequestBody double temperature) {
        HttpHeaders headers = createTestHeader();
        regulator.setTemperature(temperature);
        return ResponseEntity.ok().headers(headers).body("Temperature set successfully");
    }

    @GetMapping("/current")
    public ResponseEntity<Double> getLastTemperature() {
        HttpHeaders headers = createTestHeader();
        List<Double> temperatures = regulator.getTemperatureValues();
        Double currentTemperature;
        if (temperatures.size() > 0) {
            currentTemperature = temperatures.get(temperatures.size() - 1);
            return ResponseEntity.ok().headers(headers).body(currentTemperature);
        }
        return ResponseEntity.notFound().headers(headers).build();
    }

    @GetMapping("/last")
    public ResponseEntity<Double> getLastTemperatureValues(@RequestParam("offset") int offset,
                                                           @RequestParam("count") int count) {
        HttpHeaders headers = createTestHeader();
        List<Double> temperatures = regulator.getTemperatureValues();
        int totalTemperatures = temperatures.size();

        if (totalTemperatures > 0 && offset >= 0 && count > 0) {
            int startIndex = Math.max(totalTemperatures - offset - count, 0);
            int endIndex = Math.min(totalTemperatures - offset, totalTemperatures);

            if (endIndex >= startIndex) {
                List<Double> selectedTemperatures = temperatures.subList(startIndex, endIndex);
                return ResponseEntity.ok().headers(headers).body(selectedTemperatures.get(selectedTemperatures.size() - 1));
            }
        }
        return ResponseEntity.notFound().headers(headers).build();
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearTemperatureHistory() {
        HttpHeaders headers = createTestHeader();
        regulator.clearTemperatureValues();
        return ResponseEntity.ok().headers(headers).body("Temperature history cleared");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Double>> getAllTemperatures() {
        HttpHeaders headers = createTestHeader();
        List<Double> temperatures = regulator.getTemperatureValues();
        if (temperatures.size() > 0) {
            return ResponseEntity.ok().headers(headers).body(temperatures);
        }
        return ResponseEntity.notFound().headers(headers).build();
    }

}
