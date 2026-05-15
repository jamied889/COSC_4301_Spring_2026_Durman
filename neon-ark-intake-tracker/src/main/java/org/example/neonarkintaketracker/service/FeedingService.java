package org.example.neonarkintaketracker.service;

import org.example.neonarkintaketracker.dto.FeedingResponse;
import org.example.neonarkintaketracker.entity.Creature;
import org.example.neonarkintaketracker.entity.FeedingSchedule;
import org.example.neonarkintaketracker.exception.BadRequestException;
import org.example.neonarkintaketracker.repository.CreatureRepository;
import org.example.neonarkintaketracker.repository.FeedingScheduleRepository;
import org.example.neonarkintaketracker.repository.HabitatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class FeedingService {

    private final FeedingScheduleRepository feedingScheduleRepository;
    private final CreatureRepository creatureRepository;
    private final HabitatRepository habitatRepository;

    public FeedingService(FeedingScheduleRepository feedingScheduleRepository,
                          CreatureRepository creatureRepository,
                          HabitatRepository habitatRepository) {
        this.feedingScheduleRepository = feedingScheduleRepository;
        this.creatureRepository = creatureRepository;
        this.habitatRepository = habitatRepository;
    }

    public List<FeedingResponse> findCreaturesByFeedingTime(String time) {
        LocalTime feedingTime;

        if (time == null || time.trim().isEmpty()) {
            throw new BadRequestException("Feeding time is required.");
        }

        try {
            feedingTime = LocalTime.parse(time);
        } catch (DateTimeParseException exception) {
            throw new BadRequestException("Feeding time must use HH:MM format.");
        }

        return feedingScheduleRepository.findByFeedingTime(feedingTime)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private FeedingResponse mapToResponse(FeedingSchedule schedule) {
        FeedingResponse response;
        Creature creature;

        creature = creatureRepository.findById(schedule.getCreatureId())
                .orElse(null);

        response = new FeedingResponse();
        response.setCreatureId(schedule.getCreatureId());
        response.setFeedingTime(schedule.getFeedingTime().toString());
        response.setFood(schedule.getFood());

        if (creature != null) {
            response.setCreatureName(creature.getName());

            habitatRepository.findById(creature.getHabitatId())
                    .ifPresent(habitat -> response.setHabitatName(habitat.getName()));
        }

        return response;
    }
}
