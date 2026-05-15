-- HABITATS
INSERT INTO habitats (name, biome, zone, min_temp_c, max_temp_c)
VALUES
    ('Zone A Habitat', 'FOREST', 'Zone A', 10, 25),
    ('Zone B Habitat', 'RIVER', 'Zone B', 50, 120),
    ('Zone C Habitat', 'DESERT', 'Zone C', -40, 0)
    ON CONFLICT DO NOTHING;

-- CREATURES
INSERT INTO creatures (name, species, danger_level, condition, notes, habitat_id, created_at, status)
VALUES
    ('Drako', 'Dragon', 'HIGH', 'STABLE', 'Fire-breathing', 1, NOW(), 'ACTIVE'),
    ('Frosty', 'Ice Golem', 'MEDIUM', 'STABLE', 'Slow but strong', 3, NOW(), 'ACTIVE'),
    ('Blaze', 'Phoenix', 'HIGH', 'CRITICAL', 'Needs monitoring', 2, NOW(), 'ACTIVE')
    ON CONFLICT DO NOTHING;

-- FEEDING SCHEDULES
INSERT INTO feeding_schedules (creature_id, feeding_time, food)
VALUES
    (1, '08:00', 'Meat'),
    (2, '08:00', 'Ice Crystals'),
    (3, '12:00', 'Ash')
    ON CONFLICT DO NOTHING;

-- OBSERVATIONS
INSERT INTO observations (creature_id, user_id, note, created_at)
VALUES
    (1, 1, 'Aggressive behavior observed', NOW()),
    (1, 2, 'Calmed after feeding', NOW()),
    (2, 1, 'Very slow movement', NOW()),
    (3, 3, 'Unstable flames', NOW())
    ON CONFLICT DO NOTHING;