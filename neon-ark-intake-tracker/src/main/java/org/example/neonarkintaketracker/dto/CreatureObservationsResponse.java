package org.example.neonarkintaketracker.dto;

import java.util.List;

public class CreatureObservationsResponse {

    private Long creatureId;
    private String creatureName;
    private String habitatName;
    private List<ObservationResponse> observations;

    public Long getCreatureId() {
        return creatureId;
    }

    public void setCreatureId(Long creatureId) {
        this.creatureId = creatureId;
    }

    public String getCreatureName() {
        return creatureName;
    }

    public void setCreatureName(String creatureName) {
        this.creatureName = creatureName;
    }

    public String getHabitatName() {
        return habitatName;
    }

    public void setHabitatName(String habitatName) {
        this.habitatName = habitatName;
    }

    public List<ObservationResponse> getObservations() {
        return observations;
    }

    public void setObservations(List<ObservationResponse> observations) {
        this.observations = observations;
    }
}
