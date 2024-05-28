CREATE TABLE IF NOT EXISTS chat_group (
    jid VARCHAR(100) PRIMARY KEY,
    name VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS event (
    id BIGSERIAL PRIMARY KEY,
    chat_group_jid VARCHAR(100),
    event_date DATE NOT NULL,
    start_at TIME NOT NULL,
    end_at TIME,
    template VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (chat_group_jid) REFERENCES chat_group(jid)
);

CREATE TABLE IF NOT EXISTS person (
    jid VARCHAR(100) PRIMARY KEY,
    event_id VARCHAR(100),
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (event_id) REFERENCES event(id)
);
