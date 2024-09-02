package org.imaginnovate.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class BatchProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String processName;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private String processedFileName;
    private int insertedRecordCount;
    private int updatedRecordCount;
    private int erroredRecordCount;

    // Optional field for tracking status
    private String status; // Added to track the status of the batch process

    // Getters and Setters
}