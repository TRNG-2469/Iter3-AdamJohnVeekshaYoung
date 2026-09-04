package com.revature.ERS2.services;

import com.revature.ERS2.dtos.requests.CreateReimbursementReq;
import com.revature.ERS2.dtos.requests.PatchReimbursementReq;
import com.revature.ERS2.dtos.requests.ResolveReimbursementReq;
import com.revature.ERS2.dtos.responses.ReimbursementResponse;
import com.revature.ERS2.models.Reimbursement;
import com.revature.ERS2.models.ReimbursementStatus;
import com.revature.ERS2.models.User;

import java.util.List;

public interface ReimbursementService {

    List<ReimbursementResponse> getAllReimbursements();
    ReimbursementResponse getReimbursementById(int reimbursementID);
    List<ReimbursementResponse> getReimbursementHistory();

    List<ReimbursementResponse> getManagerOwnedReimbursements(String username);

    List<ReimbursementResponse> getReimbursements(ReimbursementStatus status, Integer departmentId, String username);
    List<ReimbursementResponse> getReimbursementsByStatus(ReimbursementStatus status);
    List<ReimbursementResponse> getReimbursementsByDepartment(Integer departmentId);
    List<ReimbursementResponse> getReimbursementsByStatusAndDepartment(ReimbursementStatus status, Integer departmentId);
    List<ReimbursementResponse> getReimbursementsByAuthor(int authorId);
    List<ReimbursementResponse> getReimbursementsByAuthorAndStatus(int authorId, ReimbursementStatus status);

    ReimbursementResponse createReimbursement(CreateReimbursementReq rDTO, String username);
    ReimbursementResponse updateReimbursement(PatchReimbursementReq r, String username, int reimbursement_id);
    void deleteReimbursement(int reimbursementID, String username);

    ReimbursementResponse resolveReimbursement(ResolveReimbursementReq rDTO, int reimbursementID, String username);

}



