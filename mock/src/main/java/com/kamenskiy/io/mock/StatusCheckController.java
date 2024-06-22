package com.kamenskiy.io.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/response")
public class StatusCheckController {

    @GetMapping("/200")
    public ResponseEntity<String> statusCheck() {
        return ResponseEntity.ok().body("200");
    }

    @GetMapping("/500")
    public ResponseEntity<String> statusCheckError() {
        return ResponseEntity.internalServerError().build();
    }
}
