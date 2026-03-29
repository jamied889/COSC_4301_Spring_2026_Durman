// CreatureService.java

package org.example.neonarkintaketracker.service;

import org.example.neonarkintaketracker.entity.Creature;
import org.example.neonarkintaketracker.dto.CreatureRequest;
import org.example.neonarkintaketracker.dto.CreatureResponse;
import org.example.neonarkintaketracker.repository.CreatureRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
 * Thin service for now.
 * Keeps the controller clean and gives us a place to add
 * validation, DTO mapping, and business rules later.
 */
@Service
public class CreatureService {

    private final CreatureRepository repository;

    public CreatureService(CreatureRepository repository) {
        this.repository = repository;
    }

    // GET ALL -> return response DTOs
    public List<CreatureResponse> getAllCreatures() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET BY ID -> return response DTO
    public Optional<CreatureResponse> getCreatureById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse);
    }

    // CREATE -> Request DTO -> Entity -> Save -> Response DTO
    public CreatureResponse createCreature(CreatureRequest req) {

        // 1) Map request DTO -> entity
        Creature creature = new Creature();
        creature.setName(req.getName());
        creature.setSpecies(req.getSpecies());
        creature.setDangerLevel(req.getDangerLevel());
        creature.setCondition(req.getCondition());
        creature.setNotes(req.getNotes());
        creature.setHabitatId(req.getHabitatId());
        creature.setCreatedAt(LocalDateTime.now());

        // 2) Optional: verify habitatId exists, then set relationship
        // Habitat habitat = habitatRepository.findById(req.getHabitatId()) ...
        // creature.setHabitat(habitat);

        // 2) Save
        Creature saved = repository.save(creature);

        return mapToResponse(saved);
    }

    // THIS METHOD WAS MISSING
    private CreatureResponse mapToResponse(Creature creature) {
        CreatureResponse res = new CreatureResponse();
        res.setId(creature.getId());
        res.setName(creature.getName());
        res.setSpecies(creature.getSpecies());
        res.setDangerLevel(creature.getDangerLevel());
        res.setCondition(creature.getCondition());
        res.setNotes(creature.getNotes());
        res.setHabitatId(creature.getHabitatId());
        res.setCreatedAt(creature.getCreatedAt());
        return res;
    }
}
