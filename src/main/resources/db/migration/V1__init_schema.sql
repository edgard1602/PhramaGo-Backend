-- Extension UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Table pharmacies
CREATE TABLE pharmacies (
                            id          BIGSERIAL PRIMARY KEY,
                            nom         VARCHAR(255)   NOT NULL,
                            adresse     TEXT,
                            quartier    VARCHAR(100),
                            telephone   VARCHAR(20),
                            whatsapp    VARCHAR(20),
                            latitude    DOUBLE PRECISION NOT NULL,
                            longitude   DOUBLE PRECISION NOT NULL,
                            is_active   BOOLEAN        DEFAULT true,
                            is_partner  BOOLEAN        DEFAULT false,
                            created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
                            updated_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

-- Table gardes
CREATE TABLE gardes (
                        id            BIGSERIAL PRIMARY KEY,
                        pharmacie_id  BIGINT      NOT NULL REFERENCES pharmacies(id),
                        date_debut    DATE        NOT NULL,
                        date_fin      DATE        NOT NULL,
                        created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT chk_dates CHECK (date_fin >= date_debut)
);

-- Table signalements
CREATE TABLE signalements (
                              id            BIGSERIAL PRIMARY KEY,
                              pharmacie_id  BIGINT       NOT NULL REFERENCES pharmacies(id),
                              type_erreur   VARCHAR(50)  NOT NULL,
                              description   TEXT,
                              statut        VARCHAR(20)  DEFAULT 'EN_ATTENTE',
                              created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Index pour les recherches fréquentes
CREATE INDEX idx_pharmacies_quartier
    ON pharmacies(quartier);
CREATE INDEX idx_gardes_dates
    ON gardes(date_debut, date_fin);
CREATE INDEX idx_gardes_pharmacie
    ON gardes(pharmacie_id);
CREATE INDEX idx_signalements_pharmacie
    ON signalements(pharmacie_id);
CREATE INDEX idx_signalements_statut
    ON signalements(statut);