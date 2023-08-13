package com.kanni.ConcurrentAccessDemo.controller;

import com.kanni.ConcurrentAccessDemo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class GreetingController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/greeting")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name)  {
        return ResponseEntity.ok().body("Hello World: " + name);
    }

    @GetMapping("/test")
    public List<String> test() throws Exception {
        Instant start = Instant.now();

        List<String> response=new ArrayList<>();

        List<CompletableFuture<String>> allFutures = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            System.out.println("Invoke call = "+ i);
            allFutures.add(asyncService.callMsgService(i));
        }

        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();

        for (int i = 0; i < 5; i++) {
            response.add("response: "+i+" - " + allFutures.get(i).get().toString());
        }

        Instant finish = Instant.now();

        long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Total time: " + timeElapsed + " ms");
        return response;

    }



}