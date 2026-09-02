package com.revature.ERS2.dtos.responses;

import com.revature.ERS2.models.ReimbursementStatus;
import com.revature.ERS2.models.ReimbursementType;
import com.revature.ERS2.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReimbursementResponse {

    private Integer id;

    private Integer authorId;
    private Integer resolverId;

    private BigDecimal amount;

    private ReimbursementStatus status;
    private ReimbursementType type;

    private String description;

    private LocalDateTime submittedAt;
    private LocalDateTime resolvedAt;
}