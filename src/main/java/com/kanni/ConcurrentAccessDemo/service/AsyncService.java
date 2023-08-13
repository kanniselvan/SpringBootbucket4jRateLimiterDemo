package com.kanni.ConcurrentAccessDemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    @Autowired
    private RestTemplate restTemplate;


    @Async
    public CompletableFuture<String> callMsgService(int i) {
        //    final String msgServiceUrl = "http://localhost:8080/getPrimeNumbers?number=20000";
        final String msgServiceUrl = "http://localhost:9000/api/greeting";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-api-key", "BX001-123");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(msgServiceUrl, HttpMethod.GET, request, String.class);


          return CompletableFuture.completedFuture(response.getBody());
    }

}
