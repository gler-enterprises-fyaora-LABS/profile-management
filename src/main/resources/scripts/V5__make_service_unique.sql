ALTER TABLE service_offered
ADD CONSTRAINT uq_service_offered_name UNIQUE (name);