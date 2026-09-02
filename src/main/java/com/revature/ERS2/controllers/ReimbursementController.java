package com.revature.ERS2.controllers;

import com.revature.ERS2.dtos.requests.CreateReimbursementReq;
import com.revature.ERS2.dtos.requests.PatchReimbursementReq;
import com.revature.ERS2.dtos.requests.ResolveReimbursementReq;
import com.revature.ERS2.dtos.responses.ReimbursementResponse;
import com.revature.ERS2.models.ReimbursementStatus;
import com.revature.ERS2.services.ReimbursementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReimbursementController {

    private final ReimbursementService reimbursementService;

    public ReimbursementController(ReimbursementService reimbursementService) {
        this.reimbursementService = reimbursementService;
    }

    @GetMapping("/reimbursements/own")
    public List<ReimbursementResponse> getManagerOwnedReimbursements(Authentication authentication) {
        String username = authentication.getName();

        return reimbursementService.getManagerOwnedReimbursements(username);
    }

    @GetMapping("/reimbursements/{id}")
    public ReimbursementResponse getReimbursementById(@PathVariable int id){
        return reimbursementService.getReimbursementById(id);
    }

    @GetMapping("/reimbursements/history")
    public List<ReimbursementResponse> getReimbursementHistory(){
        return reimbursementService.getReimbursementHistory();
    }

    @GetMapping("/reimbursements")
    public ResponseEntity<List<ReimbursementResponse>> getReimbursements(@RequestParam(required = false) ReimbursementStatus status,
                                                                         @RequestParam(required = false) Integer departmentId,
                                                                         Authentication authentication) {

        String username = authentication.getName();

        List<ReimbursementResponse> reimbursements = reimbursementService.getReimbursements(status, departmentId, username);
        return ResponseEntity.ok(reimbursements);
    }

    @PostMapping("/reimbursements")
    public ResponseEntity<ReimbursementResponse> createReimbursement(@Valid @RequestBody CreateReimbursementReq rDTO,
                                                             Authentication authentication) {

        String loggedInUsername = authentication.getName();
        ReimbursementResponse responseR = reimbursementService.createReimbursement(rDTO, loggedInUsername);

        return ResponseEntity.status(201).body(responseR);
    }

    @DeleteMapping("/reimbursements/{id}")
    public ResponseEntity<Void> deleteReimbursement(@PathVariable Integer id, Authentication authentication) {

        String loggedInUsername = authentication.getName();

        reimbursementService.deleteReimbursement(id, loggedInUsername);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reimbursements/{id}")
    public ResponseEntity<ReimbursementResponse> patchReimbursement(@Valid @RequestBody PatchReimbursementReq rDTO,
                                                                    @PathVariable("id") Integer reimbursementId,
                                                                    Authentication authentication) {

        String loggedInUsername = authentication.getName();
        ReimbursementResponse rResponse = reimbursementService.updateReimbursement(rDTO, loggedInUsername, reimbursementId);
        return ResponseEntity.ok(rResponse);
    }

    @PatchMapping("/reimbursements/{id}/status")
    public ResponseEntity<ReimbursementResponse> resolveReimbursement(@Valid @RequestBody ResolveReimbursementReq rDTO,
                                                                      @PathVariable("id") Integer reimbursementId,
                                                                      Authentication authentication) {

        String loggedInUsername = authentication.getName();
        ReimbursementResponse rResponse = reimbursementService.resolveReimbursement(rDTO, reimbursementId, loggedInUsername);
        return ResponseEntity.ok(rResponse);
    }
}
