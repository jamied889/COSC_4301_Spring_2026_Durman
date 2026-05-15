ALTER TABLE creatures
    ADD COLUMN status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE creatures
    ADD CONSTRAINT chk_creatures_status
        CHECK (status IN ('ACTIVE', 'REMOVED'));