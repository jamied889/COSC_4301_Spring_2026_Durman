package org.example.neonarkintaketracker.controller;

import org.example.neonarkintaketracker.dto.FeedingResponse;
import org.example.neonarkintaketracker.service.FeedingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedings")
public class FeedingController {

    private final FeedingService feedingService;

    public FeedingController(FeedingService feedingService) {
        this.feedingService = feedingService;
    }

    @GetMapping
    public ResponseEntity<List<FeedingResponse>> findByTime(@RequestParam String time) {
        return ResponseEntity.ok(feedingService.findCreaturesByFeedingTime(time));
    }
}
