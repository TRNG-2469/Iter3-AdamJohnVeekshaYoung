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
import com.revature.ERS2.models.ReimbursementType;
import com.revature.ERS2.models.User;
import com.revature.ERS2.repositories.ReimbursementRepository;
import com.revature.ERS2.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReimbursementServiceImplTest {

    @Mock
    private ReimbursementRepository reimbursementRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReimbursementServiceImpl reimbursementService;

    private User author;
    private User resolver;
    private Reimbursement reimbursement;
    private LocalDateTime submittedAt;

    @BeforeEach
    void setUp() {
        author = mock(User.class);
        resolver = mock(User.class);

        lenient().when(author.getId()).thenReturn(1);
        lenient().when(resolver.getId()).thenReturn(2);

        submittedAt = LocalDateTime.now();

        reimbursement = mock(Reimbursement.class);

        lenient().when(reimbursement.getId()).thenReturn(100);
        lenient().when(reimbursement.getAuthor()).thenReturn(author);
        lenient().when(reimbursement.getResolver()).thenReturn(null);
        lenient().when(reimbursement.getAmount()).thenReturn(new BigDecimal("250.00"));
        lenient().when(reimbursement.getStatus()).thenReturn(ReimbursementStatus.PENDING);
        lenient().when(reimbursement.getType()).thenReturn(ReimbursementType.TRAVEL);
        lenient().when(reimbursement.getDescription()).thenReturn("Business trip");
        lenient().when(reimbursement.getSubmittedAt()).thenReturn(submittedAt);
        lenient().when(reimbursement.getResolvedAt()).thenReturn(null);
    }

    @Test
    void testGetAllReimbursements() {
        when(reimbursementRepository.findAll())
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getAllReimbursements();

        assertNotNull(result);
        assertEquals(1, result.size());

        ReimbursementResponse response = result.get(0);

        assertEquals(100, response.getId());
        assertEquals(1, response.getAuthorId());
        assertNull(response.getResolverId());
        assertEquals(new BigDecimal("250.00"), response.getAmount());
        assertEquals(ReimbursementStatus.PENDING, response.getStatus());
        assertEquals(ReimbursementType.TRAVEL, response.getType());
        assertEquals("Business trip", response.getDescription());

        verify(reimbursementRepository).findAll();
    }

    @Test
    void testGetReimbursementById() {
        when(reimbursementRepository.findById(100))
                .thenReturn(Optional.of(reimbursement));

        ReimbursementResponse result =
                reimbursementService.getReimbursementById(100);

        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals(1, result.getAuthorId());

        verify(reimbursementRepository).findById(100);
    }

    @Test
    void testGetReimbursementByIdThrowsWhenNotFound() {
        when(reimbursementRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ReimbursementNotFoundException.class,
                () -> reimbursementService.getReimbursementById(999)
        );

        verify(reimbursementRepository).findById(999);
    }

    @Test
    void testGetReimbursementHistory() {
        Reimbursement approved = mock(Reimbursement.class);
        Reimbursement denied = mock(Reimbursement.class);

        when(approved.getId()).thenReturn(101);
        when(approved.getAuthor()).thenReturn(author);
        when(approved.getResolver()).thenReturn(resolver);
        when(approved.getAmount()).thenReturn(new BigDecimal("500.00"));
        when(approved.getStatus()).thenReturn(ReimbursementStatus.APPROVED);
        when(approved.getType()).thenReturn(ReimbursementType.TRAVEL);
        when(approved.getDescription()).thenReturn("Approved trip");
        when(approved.getSubmittedAt()).thenReturn(submittedAt);
        when(approved.getResolvedAt()).thenReturn(submittedAt);

        when(denied.getId()).thenReturn(102);
        when(denied.getAuthor()).thenReturn(author);
        when(denied.getResolver()).thenReturn(resolver);
        when(denied.getAmount()).thenReturn(new BigDecimal("100.00"));
        when(denied.getStatus()).thenReturn(ReimbursementStatus.DENIED);
        when(denied.getType()).thenReturn(ReimbursementType.FOOD);
        when(denied.getDescription()).thenReturn("Denied food");
        when(denied.getSubmittedAt()).thenReturn(submittedAt);
        when(denied.getResolvedAt()).thenReturn(submittedAt);

        when(reimbursementRepository.getResolvedHistory(
                List.of(ReimbursementStatus.APPROVED, ReimbursementStatus.DENIED)))
                .thenReturn(List.of(approved,denied));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementHistory();

        assertEquals(2, result.size());
        assertEquals(101, result.get(0).getId());
        assertEquals(102, result.get(1).getId());

        verify(reimbursementRepository)
                .getResolvedHistory(List.of(ReimbursementStatus.APPROVED, ReimbursementStatus.DENIED));
    }

    @Test
    void testGetReimbursementsWithStatusAndDepartment() {
        when(author.getUsername()).thenReturn("john_doe");

        when(userRepository.findByUsername(author.getUsername())).thenReturn(Optional.ofNullable(author));

        when(reimbursementRepository
                .findByStatusAndAuthor_Department_DepartmentId(
                        ReimbursementStatus.APPROVED, 10))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursements(
                        ReimbursementStatus.APPROVED, 10,author.getUsername());

        assertEquals(1, result.size());

        verify(reimbursementRepository)
                .findByStatusAndAuthor_Department_DepartmentId(
                        ReimbursementStatus.APPROVED, 10);
    }

    @Test
    void testGetReimbursementsWithStatusOnly() {
        when(author.getUsername()).thenReturn("john_doe");

        when(userRepository.findByUsername(author.getUsername())).thenReturn(Optional.ofNullable(author));

        when(reimbursementRepository.findByStatus(
                ReimbursementStatus.APPROVED))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursements(
                        ReimbursementStatus.APPROVED, null,author.getUsername());

        assertEquals(1, result.size());

        verify(reimbursementRepository)
                .findByStatus(ReimbursementStatus.APPROVED);
    }

    @Test
    void testGetReimbursementsWithDepartmentOnly() {
        when(author.getUsername()).thenReturn("john_doe");

        when(userRepository.findByUsername(author.getUsername())).thenReturn(Optional.ofNullable(author));

        when(reimbursementRepository
                .findByAuthor_Department_DepartmentId(10))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursements(null, 10, author.getUsername());

        assertEquals(1, result.size());

        verify(reimbursementRepository)
                .findByAuthor_Department_DepartmentId(10);
    }

    @Test
    void testGetReimbursementsWithNoFilters() {
        when(author.getUsername()).thenReturn("john_doe");

        when(userRepository.findByUsername(author.getUsername())).thenReturn(Optional.ofNullable(author));

        when(reimbursementRepository.findAll())
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursements(null, null, author.getUsername());

        assertEquals(1, result.size());

        verify(reimbursementRepository).findAll();
    }

    @Test
    void testGetReimbursementsByStatus() {
        when(reimbursementRepository.findByStatus(
                ReimbursementStatus.PENDING))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementsByStatus(
                        ReimbursementStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(ReimbursementStatus.PENDING,
                result.get(0).getStatus());

        verify(reimbursementRepository)
                .findByStatus(ReimbursementStatus.PENDING);
    }

    @Test
    void testGetReimbursementsByDepartment() {
        when(reimbursementRepository
                .findByAuthor_Department_DepartmentId(5))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementsByDepartment(5);

        assertEquals(1, result.size());

        verify(reimbursementRepository)
                .findByAuthor_Department_DepartmentId(5);
    }

    @Test
    void testGetReimbursementsByStatusAndDepartment() {
        when(reimbursementRepository
                .findByStatusAndAuthor_Department_DepartmentId(
                        ReimbursementStatus.DENIED, 5))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementsByStatusAndDepartment(
                        ReimbursementStatus.DENIED, 5);

        assertEquals(1, result.size());

        verify(reimbursementRepository)
                .findByStatusAndAuthor_Department_DepartmentId(
                        ReimbursementStatus.DENIED, 5);
    }

    @Test
    void testGetReimbursementsByAuthor() {
        when(reimbursementRepository.findByAuthor_Id(1))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementsByAuthor(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAuthorId());

        verify(reimbursementRepository).findByAuthor_Id(1);
    }

    @Test
    void testGetReimbursementsByAuthorAndStatus() {
        when(reimbursementRepository.findByAuthor_IdAndStatus(
                1, ReimbursementStatus.PENDING))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementsByAuthorAndStatus(
                        1, ReimbursementStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(ReimbursementStatus.PENDING,
                result.get(0).getStatus());

        verify(reimbursementRepository)
                .findByAuthor_IdAndStatus(
                        1, ReimbursementStatus.PENDING);
    }

    @Test
    void testCreateReimbursement() {
        CreateReimbursementReq request =
                mock(CreateReimbursementReq.class);

        when(request.getAmount()).thenReturn(new BigDecimal("300.00"));
        when(request.getType()).thenReturn(ReimbursementType.TRAVEL);
        when(request.getDescription()).thenReturn("Hotel");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(author));

        when(reimbursementRepository.save(any(Reimbursement.class)))
                .thenReturn(reimbursement);

        ReimbursementResponse result =
                reimbursementService.createReimbursement(request, "john");

        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals(1, result.getAuthorId());

        verify(userRepository).findByUsername("john");
        verify(reimbursementRepository).save(any(Reimbursement.class));
    }

    @Test
    void testCreateReimbursementThrowsWhenUserNotFound() {
        CreateReimbursementReq request =
                mock(CreateReimbursementReq.class);

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> reimbursementService
                        .createReimbursement(request, "unknown")
        );

        verify(userRepository).findByUsername("unknown");
        verify(reimbursementRepository, never())
                .save(any(Reimbursement.class));
    }

    @Test
    void testUpdateReimbursement() {
        PatchReimbursementReq request =
                mock(PatchReimbursementReq.class);

        when(request.getAmount()).thenReturn(new BigDecimal("450.00"));
        when(request.getType()).thenReturn(ReimbursementType.LODGING);
        when(request.getDescription()).thenReturn("Updated hotel");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(author));

        when(reimbursementRepository.findById(100))
                .thenReturn(Optional.of(reimbursement));

        when(reimbursementRepository.save(reimbursement))
                .thenReturn(reimbursement);

        ReimbursementResponse result =
                reimbursementService.updateReimbursement(
                        request, "john", 100);

        assertNotNull(result);

        verify(userRepository).findByUsername("john");
        verify(reimbursementRepository).findById(100);

        verify(reimbursement).setAmount(new BigDecimal("450.00"));
        verify(reimbursement).setType(ReimbursementType.LODGING);
        verify(reimbursement).setDescription("Updated hotel");

        verify(reimbursementRepository).save(reimbursement);
    }

    @Test
    void testUpdateReimbursementThrowsWhenUserNotFound() {
        PatchReimbursementReq request =
                mock(PatchReimbursementReq.class);

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> reimbursementService.updateReimbursement(
                        request, "unknown", 100)
        );

        verify(reimbursementRepository, never()).findById(anyInt());
        verify(reimbursementRepository, never())
                .save(any(Reimbursement.class));
    }

    @Test
    void testUpdateReimbursementThrowsWhenReimbursementNotFound() {
        PatchReimbursementReq request =
                mock(PatchReimbursementReq.class);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(author));

        when(reimbursementRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ReimbursementNotFoundException.class,
                () -> reimbursementService.updateReimbursement(
                        request, "john", 999)
        );

        verify(userRepository).findByUsername("john");
        verify(reimbursementRepository).findById(999);
        verify(reimbursementRepository, never())
                .save(any(Reimbursement.class));
    }

    @Test
    void testDeleteReimbursement() {
        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(author));

        when(reimbursementRepository.findById(100))
                .thenReturn(Optional.of(reimbursement));

        reimbursementService.deleteReimbursement(100, "john");

        verify(reimbursementRepository).deleteById(100);
    }

    @Test
    void testDeleteReimbursementDoesNotDeleteWhenUserIsNotAuthor() {
        User differentUser = mock(User.class);

        when(differentUser.getId()).thenReturn(99);

        when(userRepository.findByUsername("jane"))
                .thenReturn(Optional.of(differentUser));

        when(reimbursementRepository.findById(100))
                .thenReturn(Optional.of(reimbursement));

        assertThrows(
                ForbiddenException.class,
                () -> reimbursementService.deleteReimbursement(100, "jane")
        );

        verify(reimbursementRepository, never())
                .deleteById(anyInt());
    }

    @Test
    void testDeleteReimbursementThrowsWhenUserNotFound() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> reimbursementService
                        .deleteReimbursement(100, "unknown")
        );

        verify(reimbursementRepository, never())
                .findById(anyInt());
    }

    @Test
    void testDeleteReimbursementThrowsWhenReimbursementNotFound() {
        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(author));

        when(reimbursementRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ReimbursementNotFoundException.class,
                () -> reimbursementService
                        .deleteReimbursement(999, "john")
        );

        verify(reimbursementRepository, never())
                .deleteById(anyInt());
    }

    @Test
    void testResolveReimbursement() {
        ResolveReimbursementReq request =
                mock(ResolveReimbursementReq.class);

        when(request.getStatus())
                .thenReturn(ReimbursementStatus.APPROVED);

        when(userRepository.findByUsername("manager"))
                .thenReturn(Optional.of(resolver));

        when(reimbursementRepository.findById(100))
                .thenReturn(Optional.of(reimbursement));

        when(reimbursementRepository.save(reimbursement))
                .thenReturn(reimbursement);

        ReimbursementResponse result =
                reimbursementService.resolveReimbursement(
                        request, 100, "manager");

        assertNotNull(result);

        verify(userRepository).findByUsername("manager");
        verify(reimbursementRepository).findById(100);

        verify(reimbursement)
                .setResolver(resolver);

        verify(reimbursement)
                .setStatus(ReimbursementStatus.APPROVED);

        verify(reimbursementRepository).save(reimbursement);
    }

    @Test
    void testResolveReimbursementThrowsWhenUserNotFound() {
        ResolveReimbursementReq request =
                mock(ResolveReimbursementReq.class);

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> reimbursementService.resolveReimbursement(
                        request, 100, "unknown")
        );

        verify(reimbursementRepository, never())
                .findById(anyInt());

        verify(reimbursementRepository, never())
                .save(any(Reimbursement.class));
    }

    @Test
    void testResolveReimbursementThrowsWhenReimbursementNotFound() {
        ResolveReimbursementReq request =
                mock(ResolveReimbursementReq.class);

        when(userRepository.findByUsername("manager"))
                .thenReturn(Optional.of(resolver));

        when(reimbursementRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ReimbursementNotFoundException.class,
                () -> reimbursementService.resolveReimbursement(
                        request, 999, "manager")
        );

        verify(reimbursementRepository, never())
                .save(any(Reimbursement.class));
    }

    @Test
    void testResolveReimbursementThrowsWhenAlreadyResolved() {
        ResolveReimbursementReq request =
                mock(ResolveReimbursementReq.class);

        lenient().when(request.getStatus())
                .thenReturn(ReimbursementStatus.APPROVED);

        when(userRepository.findByUsername("manager"))
                .thenReturn(Optional.of(resolver));

        when(reimbursementRepository.findById(100))
                .thenReturn(Optional.of(reimbursement));

        when(reimbursement.getStatus())
                .thenReturn(ReimbursementStatus.DENIED);

        assertThrows(
                IllegalStateException.class,
                () -> reimbursementService.resolveReimbursement(
                        request, 100, "manager")
        );

        verify(reimbursementRepository, never())
                .save(any(Reimbursement.class));
    }

    @Test
    void testResolveReimbursementThrowsWhenStatusIsPending() {
        ResolveReimbursementReq request =
                mock(ResolveReimbursementReq.class);

        when(request.getStatus())
                .thenReturn(ReimbursementStatus.PENDING);

        when(userRepository.findByUsername("manager"))
                .thenReturn(Optional.of(resolver));

        when(reimbursementRepository.findById(100))
                .thenReturn(Optional.of(reimbursement));

        assertThrows(
                IllegalArgumentException.class,
                () -> reimbursementService.resolveReimbursement(
                        request, 100, "manager")
        );

        verify(reimbursementRepository, never())
                .save(any(Reimbursement.class));
    }

    @Test
    void testTransformReimbursementToResponse() {
        ReimbursementResponse result =
                ReimbursementServiceImpl
                        .transformReimbursementToResponse(reimbursement);

        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals(1, result.getAuthorId());
        assertNull(result.getResolverId());
        assertEquals(new BigDecimal("250.00"), result.getAmount());
        assertEquals(ReimbursementStatus.PENDING, result.getStatus());
        assertEquals(ReimbursementType.TRAVEL, result.getType());
        assertEquals("Business trip", result.getDescription());
        assertEquals(submittedAt, result.getSubmittedAt());
        assertNull(result.getResolvedAt());
    }

    @Test
    void testTransformReimbursementListToResponseList() {
        Reimbursement secondReimbursement =
                mock(Reimbursement.class);

        when(secondReimbursement.getId()).thenReturn(200);
        when(secondReimbursement.getAuthor()).thenReturn(author);
        when(secondReimbursement.getResolver()).thenReturn(null);
        when(secondReimbursement.getAmount()).thenReturn(new BigDecimal("100.00"));
        when(secondReimbursement.getStatus())
                .thenReturn(ReimbursementStatus.APPROVED);
        when(secondReimbursement.getType()).thenReturn(ReimbursementType.FOOD);
        when(secondReimbursement.getDescription())
                .thenReturn("Dinner");
        when(secondReimbursement.getSubmittedAt())
                .thenReturn(submittedAt);
        when(secondReimbursement.getResolvedAt())
                .thenReturn(submittedAt);

        List<Reimbursement> reimbursements =
                Arrays.asList(reimbursement, secondReimbursement);

        List<ReimbursementResponse> result =
                ReimbursementServiceImpl
                        .transformReimbursementToResponse(reimbursements);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(100, result.get(0).getId());
        assertEquals(200, result.get(1).getId());

        assertEquals(ReimbursementStatus.PENDING,
                result.get(0).getStatus());

        assertEquals(ReimbursementStatus.APPROVED,
                result.get(1).getStatus());
    }

}
