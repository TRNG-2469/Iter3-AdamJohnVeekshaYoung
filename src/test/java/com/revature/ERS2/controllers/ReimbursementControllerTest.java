package com.revature.ERS2.controllers;

import com.revature.ERS2.dtos.requests.CreateReimbursementReq;
import com.revature.ERS2.dtos.requests.PatchReimbursementReq;
import com.revature.ERS2.dtos.requests.ResolveReimbursementReq;
import com.revature.ERS2.dtos.responses.ReimbursementResponse;
import com.revature.ERS2.models.ReimbursementStatus;
import com.revature.ERS2.models.ReimbursementType;
import com.revature.ERS2.services.ReimbursementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReimbursementControllerTest {

    @Mock
    private ReimbursementService reimbursementService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReimbursementController reimbursementController;

    private ReimbursementResponse reimbursementResponse;
    private LocalDateTime submittedAt;

    @BeforeEach
    void setUp() {
        submittedAt = LocalDateTime.now();

        reimbursementResponse = new ReimbursementResponse(
                100,
                1,
                null,
                new BigDecimal("250.00"),
                ReimbursementStatus.PENDING,
                ReimbursementType.TRAVEL,
                "Business trip",
                submittedAt,
                null
        );
    }

    @Test
    void testGetReimbursementById() {
        when(reimbursementService.getReimbursementById(100))
                .thenReturn(reimbursementResponse);

        ReimbursementResponse result =
                reimbursementController.getReimbursementById(100);

        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals(1, result.getAuthorId());
        assertEquals(new BigDecimal("250.00"), result.getAmount());
        assertEquals(ReimbursementStatus.PENDING, result.getStatus());
        assertEquals(ReimbursementType.TRAVEL, result.getType());
        assertEquals("Business trip", result.getDescription());

        verify(reimbursementService).getReimbursementById(100);
    }

    @Test
    void testGetReimbursementHistory() {
        ReimbursementResponse approved = new ReimbursementResponse(
                101,
                1,
                2,
                new BigDecimal("500.00"),
                ReimbursementStatus.APPROVED,
                ReimbursementType.TRAVEL,
                "Approved trip",
                submittedAt,
                submittedAt
        );

        ReimbursementResponse denied = new ReimbursementResponse(
                102,
                1,
                2,
                new BigDecimal("100.00"),
                ReimbursementStatus.DENIED,
                ReimbursementType.FOOD,
                "Denied food",
                submittedAt,
                submittedAt
        );

        when(reimbursementService.getReimbursementHistory())
                .thenReturn(List.of(approved, denied));

        List<ReimbursementResponse> result =
                reimbursementController.getReimbursementHistory();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(101, result.get(0).getId());
        assertEquals(ReimbursementStatus.APPROVED,
                result.get(0).getStatus());

        assertEquals(102, result.get(1).getId());
        assertEquals(ReimbursementStatus.DENIED,
                result.get(1).getStatus());

        verify(reimbursementService).getReimbursementHistory();
    }

    @Test
    void testGetReimbursementsWithStatusAndDepartment() {
        when(reimbursementService.getReimbursements(
                ReimbursementStatus.APPROVED, 10, "jDoe"))
                .thenReturn(List.of(reimbursementResponse));

        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn("jDoe");

        ResponseEntity<List<ReimbursementResponse>> result =
                reimbursementController.getReimbursements(
                        ReimbursementStatus.APPROVED, 10, authentication);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(100, result.getBody().get(0).getId());

        verify(reimbursementService)
                .getReimbursements(ReimbursementStatus.APPROVED, 10, "jDoe");
    }

    @Test
    void testGetReimbursementsWithStatusOnly() {
        when(reimbursementService.getReimbursements(
                ReimbursementStatus.PENDING, null, "jDoe"))
                .thenReturn(List.of(reimbursementResponse));

        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn("jDoe");

        ResponseEntity<List<ReimbursementResponse>> result =
                reimbursementController.getReimbursements(
                        ReimbursementStatus.PENDING, null, authentication);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());

        verify(reimbursementService)
                .getReimbursements(ReimbursementStatus.PENDING, null, "jDoe");
    }

    @Test
    void testGetReimbursementsWithDepartmentOnly() {
        when(reimbursementService.getReimbursements(null, 10, "jDoe"))
                .thenReturn(List.of(reimbursementResponse));

        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn("jDoe");

        ResponseEntity<List<ReimbursementResponse>> result =
                reimbursementController.getReimbursements(null, 10, authentication);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());

        verify(reimbursementService)
                .getReimbursements(null, 10, "jDoe");
    }

    @Test
    void testGetReimbursementsWithNoFilters() {
        when(reimbursementService.getReimbursements(null, null, "jDoe"))
                .thenReturn(List.of(reimbursementResponse));

        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn("jDoe");

        ResponseEntity<List<ReimbursementResponse>> result =
                reimbursementController.getReimbursements(null, null, authentication);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());

        verify(reimbursementService)
                .getReimbursements(null, null, "jDoe");
    }

    @Test
    void testCreateReimbursement() {
        CreateReimbursementReq request =
                mock(CreateReimbursementReq.class);

        when(authentication.getName())
                .thenReturn("john");

        when(reimbursementService.createReimbursement(
                request, "john"))
                .thenReturn(reimbursementResponse);

        ResponseEntity<ReimbursementResponse> result =
                reimbursementController.createReimbursement(
                        request, authentication);

        assertNotNull(result);
        assertEquals(201, result.getStatusCode().value());
        assertNotNull(result.getBody());

        assertEquals(100, result.getBody().getId());
        assertEquals(1, result.getBody().getAuthorId());
        assertEquals(ReimbursementStatus.PENDING,
                result.getBody().getStatus());

        verify(authentication).getName();
        verify(reimbursementService)
                .createReimbursement(request, "john");
    }

    @Test
    void testDeleteReimbursement() {
        when(authentication.getName())
                .thenReturn("john");

        doNothing().when(reimbursementService)
                .deleteReimbursement(100, "john");

        ResponseEntity<Void> result =
                reimbursementController.deleteReimbursement(
                        100, authentication);

        assertNotNull(result);
        assertEquals(204, result.getStatusCode().value());
        assertNull(result.getBody());

        verify(authentication).getName();
        verify(reimbursementService)
                .deleteReimbursement(100, "john");
    }

    @Test
    void testPatchReimbursement() {
        PatchReimbursementReq request =
                mock(PatchReimbursementReq.class);

        ReimbursementResponse updatedResponse =
                new ReimbursementResponse(
                        100,
                        1,
                        null,
                        new BigDecimal("450.00"),
                        ReimbursementStatus.PENDING,
                        ReimbursementType.LODGING,
                        "Updated hotel",
                        submittedAt,
                        null
                );

        when(authentication.getName())
                .thenReturn("john");

        when(reimbursementService.updateReimbursement(
                request, "john", 100))
                .thenReturn(updatedResponse);

        ResponseEntity<ReimbursementResponse> result =
                reimbursementController.patchReimbursement(
                        request, 100, authentication);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());

        assertEquals(100, result.getBody().getId());
        assertEquals(new BigDecimal("450.00"),
                result.getBody().getAmount());
        assertEquals(ReimbursementType.LODGING,
                result.getBody().getType());
        assertEquals("Updated hotel",
                result.getBody().getDescription());

        verify(authentication).getName();
        verify(reimbursementService)
                .updateReimbursement(request, "john", 100);
    }

    @Test
    void testResolveReimbursement() {
        ResolveReimbursementReq request =
                mock(ResolveReimbursementReq.class);

        ReimbursementResponse resolvedResponse =
                new ReimbursementResponse(
                        100,
                        1,
                        2,
                        new BigDecimal("250.00"),
                        ReimbursementStatus.APPROVED,
                        ReimbursementType.TRAVEL,
                        "Business trip",
                        submittedAt,
                        submittedAt
                );

        when(authentication.getName())
                .thenReturn("manager");

        when(reimbursementService.resolveReimbursement(
                request, 100, "manager"))
                .thenReturn(resolvedResponse);

        ResponseEntity<ReimbursementResponse> result =
                reimbursementController.resolveReimbursement(
                        request, 100, authentication);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());

        assertEquals(100, result.getBody().getId());
        assertEquals(1, result.getBody().getAuthorId());
        assertEquals(2, result.getBody().getResolverId());
        assertEquals(ReimbursementStatus.APPROVED,
                result.getBody().getStatus());
        assertEquals(ReimbursementType.TRAVEL,
                result.getBody().getType());

        verify(authentication).getName();
        verify(reimbursementService)
                .resolveReimbursement(request, 100, "manager");
    }

}
