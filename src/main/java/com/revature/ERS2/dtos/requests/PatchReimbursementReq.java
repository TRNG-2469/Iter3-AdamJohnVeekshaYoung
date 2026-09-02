package com.revature.ERS2.dtos.requests;

import com.revature.ERS2.models.ReimbursementType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PatchReimbursementReq {

    @DecimalMin(value = "0.01",
            message = "Amount must be a positive value")
    @DecimalMax(value = "999999.00",
            message = "Amount must be less than or equal to 999999.00")
    private BigDecimal amount;

    private ReimbursementType type;

    @Size(max = 255,
            message = "Description must be less than or equal to 255 characters")
    private String description;
}