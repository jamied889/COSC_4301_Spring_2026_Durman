// CreatureController.java

package org.example.neonarkintaketracker.controller;

import org.example.neonarkintaketracker.dto.CreatureRequest;
import org.example.neonarkintaketracker.dto.CreatureResponse;
import org.example.neonarkintaketracker.service.CreatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.neonarkintaketracker.dto.RenameCreatureRequest;
import org.example.neonarkintaketracker.dto.RenameCreatureResponse;
import org.example.neonarkintaketracker.dto.CreatureObservationsResponse;

import java.util.List;
import java.util.Optional;
//import jakarta.validation.Valid;

/*
 * This controller handles incoming HTTP requests for /api/creatures
 */
@RestController
@RequestMapping("/api/creatures")
public class CreatureController {

    private final CreatureService creatureService;

    // Constructor-based Dependency Injection (DI)
    public CreatureController(CreatureService creatureService) {
        this.creatureService = creatureService;
    }

    /*
     * Map HTTP GET requests at /api/creatures to this method
     * Example: GET http://localhost:8080/api/creatures
     */
    @GetMapping
    public ResponseEntity<List<CreatureResponse>> getAllCreatures() {

        List<CreatureResponse> creatures = creatureService.getAllCreatures();

        // Return 200 OK with JSON body
        return ResponseEntity.ok(creatures);
    }

    // NEW: GET /api/creatures/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CreatureResponse> getCreatureById(@PathVariable Long id) {

        Optional<CreatureResponse> maybeCreature = creatureService.getCreatureById(id);

        if (maybeCreature.isEmpty()) {
            // 404 when id does not exist
            return ResponseEntity.notFound().build();
        }

        // 200 OK when found
        return ResponseEntity.ok(maybeCreature.get());
    }

    @PostMapping
    public ResponseEntity<CreatureResponse> create(@RequestBody CreatureRequest req) {
        CreatureResponse created = creatureService.createCreature(req);
        return ResponseEntity.status(201).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CreatureResponse> removeCreature(@PathVariable Long id)
    {
        CreatureResponse response;

        response = creatureService.removeCreature(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<RenameCreatureResponse> renameCreature(
            @PathVariable Long id,
            @RequestBody RenameCreatureRequest request)
    {
        RenameCreatureResponse response;

        response = creatureService.renameCreature(id, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/observations")
    public ResponseEntity<CreatureObservationsResponse> getCreatureObservations(@PathVariable Long id)
    {
        CreatureObservationsResponse response;

        response = creatureService.getCreatureObservations(id);

        return ResponseEntity.ok(response);
    }
}
