CREATE TABLE habitats (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          biome VARCHAR(50),
                          zone VARCHAR(50),   -- ADD THIS BACK
                          min_temp_c INT,
                          max_temp_c INT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);