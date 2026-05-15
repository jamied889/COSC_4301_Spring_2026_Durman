package org.example.neonarkintaketracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feeding_schedules")
public class FeedingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creature_id", nullable = false)
    private Long creatureId;

    @Column(name = "feeding_time", nullable = false)
    private LocalTime feedingTime;

    @Column(nullable = false, length = 120)
    private String food;
}