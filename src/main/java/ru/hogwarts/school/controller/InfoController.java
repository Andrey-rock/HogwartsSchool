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
        // Сдесь по заданию параллельный стрим, но он не даёт положительного эффекта
        long start = System.currentTimeMillis();
        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        long finish = System.currentTimeMillis() - start;
        System.out.println("finish = " + finish);
        return sum;
    }
}
