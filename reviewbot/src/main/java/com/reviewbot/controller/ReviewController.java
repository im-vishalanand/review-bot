package com.reviewbot.controller;

import com.reviewbot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/api/reviews")
    public ResponseEntity<Map<String, Object>> getReviews(@RequestParam String page) {
        return new ResponseEntity<>(reviewService.extractReviews(page), HttpStatus.OK);
    }
}
