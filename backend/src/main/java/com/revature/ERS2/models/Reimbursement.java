package com.revature.ERS2.models;

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

@Entity
@Table(name = "reimbursements")
@Data
@NoArgsConstructor
public class Reimbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reimbursement_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading optimizes database queries
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading optimizes database queries
    @JoinColumn(name = "resolver_id")
    private User resolver;

    @DecimalMin(value = "0.01", message = "Amount must be a positive value")
    @DecimalMax(value = "999999.00", message = "Amount must be less than or equal to 999999.00")
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private ReimbursementStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required")
    private ReimbursementType type;

    @Size(max = 255, message = "Description must be less than or equal to 255 characters")
    private String description;

    @NotNull(message = "Submitted at is required")
    private LocalDateTime submittedAt;

    private LocalDateTime resolvedAt;

    //Constructor for creating reimbursements via POST
    //Users should not be able to set resolve time, id, etc.
    public Reimbursement(User author, BigDecimal amount, ReimbursementStatus status, ReimbursementType type,
                         String description, LocalDateTime submittedAt) {
        this.author = author;
        this.amount = amount;
        this.status = status;
        this.type = type;
        this.description = description;
        this.submittedAt = submittedAt;
    }



}




