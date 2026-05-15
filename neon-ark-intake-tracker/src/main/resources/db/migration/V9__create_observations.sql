CREATE TABLE IF NOT EXISTS observations (
                                            id BIGSERIAL PRIMARY KEY,
                                            creature_id BIGINT NOT NULL,
                                            user_id BIGINT NOT NULL,
                                            note TEXT NOT NULL,
                                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                            CONSTRAINT fk_observations_creature
                                            FOREIGN KEY (creature_id)
    REFERENCES creatures(id)
    );