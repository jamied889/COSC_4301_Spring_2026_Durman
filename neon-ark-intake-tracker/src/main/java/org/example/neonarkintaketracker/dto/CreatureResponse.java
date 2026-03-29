package org.example.neonarkintaketracker.dto;

/**
 * DTO used for READ responses going back to the Java CLI client.
 *
 * NOTE:
 *  - We INCLUDE id and createdAt because the server/database controls them.
 *  - This is the "allowed" shape of outgoing data.
 *  - The client can see these values, but should never be able to set them.
 */

import java.time.LocalDateTime;

public class CreatureResponse {

    private Long id;
    private String name;
    private String species;
    private String dangerLevel;
    private String condition;
    private String notes;
    private Long habitatId;
    private LocalDateTime createdAt;

    public CreatureResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getHabitatId() {
        return habitatId;
    }

    public void setHabitatId(Long habitatId) {
        this.habitatId = habitatId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
