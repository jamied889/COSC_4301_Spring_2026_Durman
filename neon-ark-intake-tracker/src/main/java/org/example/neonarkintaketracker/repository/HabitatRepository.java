package org.example.neonarkintaketracker.repository;

import jakarta.persistence.*;
import org.example.neonarkintaketracker.entity.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface HabitatRepository extends JpaRepository<Habitat, Long> {
    @Entity
    @Table(name = "habitats")
    public class Habitat {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        // getters/setters
    }
}
