CREATE TABLE IF NOT EXISTS feeding_schedules (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 creature_id BIGINT NOT NULL,
                                                 feeding_time TIME NOT NULL,
                                                 food VARCHAR(120) NOT NULL,

    CONSTRAINT fk_feeding_schedules_creature
    FOREIGN KEY (creature_id)
    REFERENCES creatures(id)
    );