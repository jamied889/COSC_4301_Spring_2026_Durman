CREATE TABLE IF NOT EXISTS system_users (
                                            id BIGSERIAL PRIMARY KEY,
                                            full_name VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL,
    phone VARCHAR(30),
    role VARCHAR(40) NOT NULL
    );

INSERT INTO system_users (full_name, email, phone, role)
VALUES
    ('Admin Warden', 'admin@neonark.local', '555-0100', 'Admin'),
    ('Field Warden', 'field@neonark.local', '555-0101', 'Field'),
    ('Rift Warden', 'rift@neonark.local', '555-0102', 'Rift')
    ON CONFLICT DO NOTHING;