package ru.practicum.main_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.main_server","ru.practicum.stats_server",
        "ru.practicum.stats_client", "ru.practicum.ewm_utils"})
public class EWMMainService {

    public static void main(String[] args) {
        SpringApplication.run(EWMMainService.class, args);
    }

}