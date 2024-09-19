package com.parsermoney.parsermoney;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class Controller {
    private final Service service;

    @GetMapping("/parse")
    public String parse () {
        try {
            service.parseAndSaveData();
            return "save";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @GetMapping("/update-money")
    public String updateMoney() {
        try {
            service.parseAndUpdateData();
            return "Money data updated successfully!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
