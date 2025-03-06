package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class InfoController {

    @Value("${server.port:0}")
    private int port;

    @GetMapping("/port")
    public int getPort() {
        return port;
    }

    @GetMapping("/sum_million")
    public int getSum() {
        return Stream.iterate(1, a -> a + 1).parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);
    }
}
