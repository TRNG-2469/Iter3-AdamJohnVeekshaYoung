package com.revature.ERS2.repositories;

import com.revature.ERS2.models.Reimbursement;
import com.revature.ERS2.models.ReimbursementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, Integer> {

    List<Reimbursement> findByStatus(ReimbursementStatus status);

    //Department id is nested within this chain, custom query for clarity? idk
    List<Reimbursement> findByAuthor_Department_DepartmentId(Integer departmentId);
    List<Reimbursement> findByStatusAndAuthor_Department_DepartmentId(ReimbursementStatus status, Integer departmentId);

    List<Reimbursement> findByAuthor_Id(Integer authorId);
    List<Reimbursement> findByAuthor_IdAndStatus(Integer authorId, ReimbursementStatus status);

    @Query("SELECT r FROM Reimbursement r WHERE r.status IN (:statuses) ORDER BY r.resolvedAt ASC")
    List<Reimbursement> getResolvedHistory(@Param("statuses") List<ReimbursementStatus> statuses);
}
