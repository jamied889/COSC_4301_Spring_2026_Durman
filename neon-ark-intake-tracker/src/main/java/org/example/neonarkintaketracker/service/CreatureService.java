// CreatureService.java

package org.example.neonarkintaketracker.service;

import org.example.neonarkintaketracker.entity.Creature;
import org.example.neonarkintaketracker.dto.CreatureRequest;
import org.example.neonarkintaketracker.dto.CreatureResponse;
import org.example.neonarkintaketracker.repository.CreatureRepository;
import org.springframework.stereotype.Service;
import org.example.neonarkintaketracker.dto.RenameCreatureRequest;
import org.example.neonarkintaketracker.dto.RenameCreatureResponse;
import org.example.neonarkintaketracker.repository.HabitatRepository;
import org.example.neonarkintaketracker.exception.ResourceNotFoundException;
import org.example.neonarkintaketracker.exception.BadRequestException;
import org.example.neonarkintaketracker.exception.ConflictException;
import org.example.neonarkintaketracker.repository.FeedingScheduleRepository;
import org.example.neonarkintaketracker.dto.CreatureObservationsResponse;
import org.example.neonarkintaketracker.dto.ObservationResponse;
import org.example.neonarkintaketracker.entity.Observation;
import org.example.neonarkintaketracker.repository.ObservationRepository;

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
    private final HabitatRepository habitatRepository;
    private final FeedingScheduleRepository feedingScheduleRepository;
    private final ObservationRepository observationRepository;

    public CreatureService(CreatureRepository repository,
                           HabitatRepository habitatRepository,
                           FeedingScheduleRepository feedingScheduleRepository,
                           ObservationRepository observationRepository) {
        this.repository = repository;
        this.habitatRepository = habitatRepository;
        this.feedingScheduleRepository = feedingScheduleRepository;
        this.observationRepository = observationRepository;
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

        if (isBlank(req.getName())) {
            throw new BadRequestException("Creature name is required.");
        }

        if (isBlank(req.getSpecies())) {
            throw new BadRequestException("Creature species is required.");
        }

        if (isBlank(req.getDangerLevel())) {
            throw new BadRequestException("Danger level is required.");
        }

        if (isBlank(req.getCondition())) {
            throw new BadRequestException("Condition is required.");
        }

        if (req.getHabitatId() == null) {
            throw new BadRequestException("Habitat id is required.");
        }

        if (repository.existsByNameIgnoreCaseAndHabitatId(req.getName(), req.getHabitatId())) {
            throw new ConflictException("A creature with that name already exists in this habitat.");
        }

        // 1) Map request DTO -> entity
        Creature creature = new Creature();
        creature.setName(req.getName());
        creature.setSpecies(req.getSpecies());
        creature.setDangerLevel(req.getDangerLevel());
        creature.setCondition(req.getCondition());
        creature.setNotes(req.getNotes());
        creature.setHabitatId(req.getHabitatId());
        creature.setCreatedAt(LocalDateTime.now());
        creature.setStatus("ACTIVE");

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
        res.setStatus(creature.getStatus());
        habitatRepository.findById(creature.getHabitatId())
                .ifPresent(habitat -> res.setHabitatName(habitat.getName()));
        return res;
    }

    public CreatureResponse removeCreature(Long id)
    {
        Creature creature;

        creature = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Creature id not found."));

        if (feedingScheduleRepository.existsByCreatureId(id)) {
            throw new ConflictException("Cannot remove creature with an active feeding schedule.");
        }

        creature.setStatus("REMOVED");

        creature = repository.save(creature);

        return mapToResponse(creature);
    }

    public RenameCreatureResponse renameCreature(Long id, RenameCreatureRequest request)
    {
        if (request == null || isBlank(request.getNewName())) {
            throw new BadRequestException("New creature name is required.");
        }

        Creature creature;
        RenameCreatureResponse response;
        String oldName;

        creature = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Creature id not found."));

        if (repository.existsByNameIgnoreCaseAndHabitatId(request.getNewName(), creature.getHabitatId())) {
            throw new ConflictException("A creature with that name already exists in this habitat.");
        }

        oldName = creature.getName();

        creature.setName(request.getNewName());

        creature = repository.save(creature);

        response = new RenameCreatureResponse();
        response.setId(creature.getId());
        response.setOldName(oldName);
        response.setNewName(creature.getName());

        return response;
    }

    public CreatureObservationsResponse getCreatureObservations(Long id)
    {
        Creature creature;
        CreatureObservationsResponse response;
        List<ObservationResponse> observations;

        creature = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Creature id not found."));

        observations = observationRepository.findByCreatureIdOrderByCreatedAtAsc(id)
                .stream()
                .map(this::mapObservationToResponse)
                .toList();

        response = new CreatureObservationsResponse();
        response.setCreatureId(creature.getId());
        response.setCreatureName(creature.getName());

        habitatRepository.findById(creature.getHabitatId())
                .ifPresent(habitat -> response.setHabitatName(habitat.getName()));

        response.setObservations(observations);

        return response;
    }

    private ObservationResponse mapObservationToResponse(Observation observation)
    {
        ObservationResponse response;

        response = new ObservationResponse();
        response.setId(observation.getId());
        response.setUserId(observation.getUserId());
        response.setAuthor("User " + observation.getUserId());
        response.setNote(observation.getNote());
        response.setCreatedAt(observation.getCreatedAt().toString());

        return response;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
