package com.revature.ERS2.dtos.requests;

import com.revature.ERS2.models.ReimbursementStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResolveReimbursementReq {

    @NotNull(message = "Status is required")
    private ReimbursementStatus status;
}
