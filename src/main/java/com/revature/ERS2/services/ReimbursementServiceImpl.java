package com.revature.ERS2.services;

import com.revature.ERS2.dtos.requests.CreateReimbursementReq;
import com.revature.ERS2.dtos.requests.PatchReimbursementReq;
import com.revature.ERS2.dtos.requests.ResolveReimbursementReq;
import com.revature.ERS2.dtos.responses.ReimbursementResponse;
import com.revature.ERS2.exceptions.ForbiddenException;
import com.revature.ERS2.exceptions.ReimbursementNotFoundException;
import com.revature.ERS2.exceptions.UserNotFoundException;
import com.revature.ERS2.models.Reimbursement;
import com.revature.ERS2.models.ReimbursementStatus;
import com.revature.ERS2.models.Role;
import com.revature.ERS2.models.User;
import com.revature.ERS2.repositories.ReimbursementRepository;
import com.revature.ERS2.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReimbursementServiceImpl implements ReimbursementService {

    private final ReimbursementRepository reimbursementRepository;
    private final UserRepository userRepository;

    public ReimbursementServiceImpl(ReimbursementRepository reimbursementRepository,
                                    UserRepository userRepository) {
        this.reimbursementRepository = reimbursementRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ReimbursementResponse> getAllReimbursements() {
        return transformReimbursementToResponse(reimbursementRepository.findAll());
    }

    @Override
    public ReimbursementResponse getReimbursementById(int reimbursementID) {
        Reimbursement reimbursement = reimbursementRepository.findById(reimbursementID)
                .orElseThrow(() -> new ReimbursementNotFoundException(reimbursementID));
        return transformReimbursementToResponse(reimbursement);
    }

    //Will not return an ordered history
    @Override
    public List<ReimbursementResponse> getReimbursementHistory() {
        return transformReimbursementToResponse(reimbursementRepository.getResolvedHistory(
                List.of(ReimbursementStatus.APPROVED, ReimbursementStatus.DENIED)));
    }

    @Override
    public List<ReimbursementResponse> getReimbursements(ReimbursementStatus status, Integer departmentId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow( () -> new UserNotFoundException(username));

        //Employee roles get routed
        if (user.getRole() == Role.EMPLOYEE) {

            if (departmentId != null) {
                throw new ForbiddenException("Employees cannot filter by department");
            }

            if (status != null) {
                return getReimbursementsByAuthorAndStatus(user.getId(), status);
            } else {
                return getReimbursementsByAuthor(user.getId());
            }
        }

        //Manager only methods below

        //Branch to the appropiate method based on the query parameters
        if (status != null && departmentId != null) {
            return getReimbursementsByStatusAndDepartment(status, departmentId);
        }

        if (status != null) {
            return getReimbursementsByStatus(status);
        }

        if (departmentId != null) {
            return getReimbursementsByDepartment(departmentId);
        }

        return getAllReimbursements();
    }

    @Override
    public List<ReimbursementResponse> getReimbursementsByStatus(ReimbursementStatus status) {
        return transformReimbursementToResponse(reimbursementRepository.findByStatus(status));
    }

    @Override
    public List<ReimbursementResponse> getReimbursementsByDepartment(Integer departmentId) {
        return transformReimbursementToResponse(reimbursementRepository.findByAuthor_Department_DepartmentId(departmentId));
    }

    @Override
    public List<ReimbursementResponse> getReimbursementsByStatusAndDepartment(ReimbursementStatus status, Integer departmentId) {
        return transformReimbursementToResponse(reimbursementRepository.findByStatusAndAuthor_Department_DepartmentId(status, departmentId));
    }

    @Override
    public List<ReimbursementResponse> getReimbursementsByAuthor(int authorId) {
        //verified author exists in parent class and is the caller

        return transformReimbursementToResponse(reimbursementRepository.findByAuthor_Id(authorId));
    }

    @Override
    public List<ReimbursementResponse> getReimbursementsByAuthorAndStatus(int authorId, ReimbursementStatus status) {
        //userRepository.findById(authorId).orElseThrow( () -> throw new RuntimeException("Author was not found"))

        return transformReimbursementToResponse(reimbursementRepository.findByAuthor_IdAndStatus(authorId, status));
    }

    @Override
    public ReimbursementResponse createReimbursement(CreateReimbursementReq rDTO, String username) {

        //First, gets logged in user's username and sees if the user even exists
        User author = userRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException(username));

        //Creates reimbursement object (special constructor for making new one from POST)
        //This nulls things users shouldn't touch like resolved time, id, etc.
        Reimbursement r = new Reimbursement(author, rDTO.getAmount(), ReimbursementStatus.PENDING,
                rDTO.getType(), rDTO.getDescription(), LocalDateTime.now());

        Reimbursement savedR = reimbursementRepository.save(r);
        log.info("Reimbursement {} created by '{}', amount={}, type={}", savedR.getId(), username, savedR.getAmount(), savedR.getType());
        //Constructs a reimbursement response (doesn't expose entire user object, just their id for frontend)
        //Wrote a helper method cuz it was repetitive and I think we only have one model for Response
        return transformReimbursementToResponse(savedR);
    }

    @Override
    public ReimbursementResponse updateReimbursement(PatchReimbursementReq r, String username, int reimbursement_id) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Reimbursement existingReimbursement = reimbursementRepository.findById(reimbursement_id)
                .orElseThrow(() -> new ReimbursementNotFoundException(reimbursement_id));

        if (!Objects.equals(existingReimbursement.getAuthor().getId(), user.getId())) {
            throw new ForbiddenException("You cannot update another person's reimbursement");
        }

        if (existingReimbursement.getStatus() != ReimbursementStatus.PENDING) {
            throw new IllegalStateException("Resolved reimbursements cannot be edited");
        }

        existingReimbursement.setAmount(r.getAmount());
        existingReimbursement.setType(r.getType());
        existingReimbursement.setDescription(r.getDescription());
        Reimbursement savedReimbursement = reimbursementRepository.save(existingReimbursement);
        log.info("Reimbursement {} updated by '{}', amount={}, type={}", savedReimbursement.getId(), username,
                savedReimbursement.getAmount(), savedReimbursement.getType());
        return transformReimbursementToResponse(savedReimbursement);
    }

    @Override
    public void deleteReimbursement(int reimbursementID, String username) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Reimbursement r = reimbursementRepository.findById(reimbursementID)
                .orElseThrow(() -> new ReimbursementNotFoundException(reimbursementID));
        if (!u.getId().equals(r.getAuthor().getId())) {
            throw new ForbiddenException("You cannot delete another user's reimbursement");
        }

        if (r.getStatus() != ReimbursementStatus.PENDING) {
            throw new IllegalStateException("Only pending reimbursements can be deleted");
        }
        log.info("Reimbursement {} deleted by '{}'", r.getId(), username);
        reimbursementRepository.deleteById(reimbursementID);

    }

    @Override
    public ReimbursementResponse resolveReimbursement(ResolveReimbursementReq rDTO, int reimbursementID, String username) {
        User resolver = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Reimbursement reimbursement = reimbursementRepository.findById(reimbursementID)
                .orElseThrow(() -> new ReimbursementNotFoundException(reimbursementID));
        if (!reimbursement.getStatus().equals(ReimbursementStatus.PENDING)) {
            throw new IllegalStateException("Cannot resolve a reimbursement that is not pending");
        }
        if (rDTO.getStatus() == ReimbursementStatus.PENDING){
            throw new IllegalArgumentException("Status cannot be set to pending");
        }

        if (reimbursement.getAuthor().getId().equals(resolver.getId())) {
            throw new ForbiddenException("Managers cannot resolve their own reimbursement");
        }

            reimbursement.setResolver(resolver);
            reimbursement.setStatus(rDTO.getStatus());
            reimbursement.setResolvedAt(LocalDateTime.now());

            Reimbursement savedReimbursement = reimbursementRepository.save(reimbursement);
            log.info("Reimbursement {} resolved by '{}'", savedReimbursement.getId(), username);
            return transformReimbursementToResponse(savedReimbursement);
    }

    @Override
    public List<ReimbursementResponse> getManagerOwnedReimbursements(String username) {
        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<Reimbursement> reimbursements =
                reimbursementRepository.findByAuthor_Id(manager.getId());

        return transformReimbursementToResponse(reimbursements);
    }


    /**
     * Transforms a reimbursement to a reimbursement response
     * (Does not expose user details)
     *
     * @param r Reimbursement
     * @return
     */
    public static ReimbursementResponse transformReimbursementToResponse(Reimbursement r) {

        Integer resolverId;

        if (r.getResolver() == null) {
            resolverId = null;
        } else {
            resolverId = r.getResolver().getId();
        }

        return new ReimbursementResponse(r.getId(), r.getAuthor().getId(),
                resolverId, r.getAmount(), r.getStatus(), r.getType(),
                r.getDescription(), r.getSubmittedAt(), r.getResolvedAt());
    }


    /**
     * Well, transforms a list of reimbursements to list of reimbursementResponse
     *
     * @param rList
     * @return
     */
    public static List<ReimbursementResponse> transformReimbursementToResponse(List<Reimbursement> rList) {

        List<ReimbursementResponse> responseList = new ArrayList<>();

        for (Reimbursement r : rList) {

            Integer resolverId;

            if (r.getResolver() == null) {
                resolverId = null;
            } else {
                resolverId = r.getResolver().getId();
            }

            responseList.add(new ReimbursementResponse(r.getId(), r.getAuthor().getId(),
                    resolverId, r.getAmount(), r.getStatus(), r.getType(),
                    r.getDescription(), r.getSubmittedAt(), r.getResolvedAt()));
        }

        return responseList;
    }



}
