package com.grzegorzfit.smartdelivery.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "processed_event")
@NoArgsConstructor
public class ProcessedEvent {

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    @Column(nullable = false, name = "event_type")
    private String eventType;

    @Column(nullable = false, name = "processed_at")
    private Instant processedAt;

    public ProcessedEvent(String eventType, UUID eventId) {
        this.eventType = eventType;
        this.eventId = eventId;
    }

    @PrePersist
    private void setCreatedAt() {
        if (processedAt == null) {
            this.processedAt = Instant.now();
        }
    }
}
