# Prompt Log

## Conversation Summary

This conversation involved generating JUnit 5 and Mockito unit tests for a Java Spring Boot reimbursement application.

---

## Prompt 1 — Generate `ReimbursementServiceImplTest`

### User Prompt

The user provided the complete `ReimbursementServiceImpl` class and an initial test-class template containing empty test methods.

The user asked:

> Generate a Java Class that tests the following `ReimbursementServiceImple` class using JUnit and Mockito, based on the given class template.

The provided service contained methods for:

* Getting all reimbursements
* Getting a reimbursement by ID
* Getting reimbursement history
* Filtering reimbursements by status
* Filtering by department
* Filtering by status and department
* Filtering by author
* Filtering by author and status
* Creating reimbursements
* Updating reimbursements
* Deleting reimbursements
* Resolving reimbursements
* Transforming reimbursements into response DTOs
* Transforming lists of reimbursements into response DTOs

The provided template used:

```java
package com.revature.ERS2.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReimbursementServiceImplTest {

    @Test
    void testGetAllReimbursements() {
    }

    @Test
    void testGetReimbursementById() {
    }

    @Test
    void testGetReimbursementHistory() {
    }

    @Test
    void testGetReimbursements() {
    }

    @Test
    void testGetReimbursementsByStatus() {
    }

    @Test
    void testGetReimbursementsByDepartment() {
    }

    @Test
    void testGetReimbursementsByStatusAndDepartment() {
    }

    @Test
    void testGetReimbursementsByAuthor() {
    }

    @Test
    void testGetReimbursementsByAuthorAndStatus() {
    }

    @Test
    void testCreateReimbursement() {
    }

    @Test
    void testUpdateReimbursement() {
    }

    @Test
    void testDeleteReimbursement() {
    }

    @Test
    void testResolveReimbursement() {
    }

    @Test
    void testTransformReimbursementToResponse() {
    }

    @Test
    void testTransformReimbursementListToResponseList() {
    }
}
```

### Assistant Response

The assistant generated the following Mockito-based unit test class:
```java
package com.revature.ERS2.services;

import com.revature.ERS2.dtos.requests.CreateReimbursementReq;
import com.revature.ERS2.dtos.requests.PatchReimbursementReq;
import com.revature.ERS2.dtos.requests.ResolveReimbursementReq;
import com.revature.ERS2.dtos.responses.ReimbursementResponse;
import com.revature.ERS2.exceptions.ReimbursementNotFoundException;
import com.revature.ERS2.exceptions.UserNotFoundException;
import com.revature.ERS2.models.Reimbursement;
import com.revature.ERS2.models.ReimbursementStatus;
import com.revature.ERS2.models.User;
import com.revature.ERS2.repositories.ReimbursementRepository;
import com.revature.ERS2.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReimbursementServiceImplTest {

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

        when(author.getId()).thenReturn(1);
        when(resolver.getId()).thenReturn(2);

        submittedAt = LocalDateTime.now();

        reimbursement = mock(Reimbursement.class);

        when(reimbursement.getId()).thenReturn(100);
        when(reimbursement.getAuthor()).thenReturn(author);
        when(reimbursement.getResolver()).thenReturn(null);
        when(reimbursement.getAmount()).thenReturn(250.00);
        when(reimbursement.getStatus()).thenReturn(ReimbursementStatus.PENDING);
        when(reimbursement.getType()).thenReturn("TRAVEL");
        when(reimbursement.getDescription()).thenReturn("Business trip");
        when(reimbursement.getSubmittedAt()).thenReturn(submittedAt);
        when(reimbursement.getResolvedAt()).thenReturn(null);
    }

    // ---------------------------------------------------------
    // getAllReimbursements
    // ---------------------------------------------------------

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
        assertEquals(250.00, response.getAmount());
        assertEquals(ReimbursementStatus.PENDING, response.getStatus());
        assertEquals("TRAVEL", response.getType());
        assertEquals("Business trip", response.getDescription());

        verify(reimbursementRepository).findAll();
    }

    // ---------------------------------------------------------
    // getReimbursementById
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // getReimbursementHistory
    // ---------------------------------------------------------

    @Test
    void testGetReimbursementHistory() {
        Reimbursement approved = mock(Reimbursement.class);
        Reimbursement denied = mock(Reimbursement.class);

        when(approved.getId()).thenReturn(101);
        when(approved.getAuthor()).thenReturn(author);
        when(approved.getResolver()).thenReturn(resolver);
        when(approved.getAmount()).thenReturn(500.00);
        when(approved.getStatus()).thenReturn(ReimbursementStatus.APPROVED);
        when(approved.getType()).thenReturn("TRAVEL");
        when(approved.getDescription()).thenReturn("Approved trip");
        when(approved.getSubmittedAt()).thenReturn(submittedAt);
        when(approved.getResolvedAt()).thenReturn(submittedAt);

        when(denied.getId()).thenReturn(102);
        when(denied.getAuthor()).thenReturn(author);
        when(denied.getResolver()).thenReturn(resolver);
        when(denied.getAmount()).thenReturn(100.00);
        when(denied.getStatus()).thenReturn(ReimbursementStatus.DENIED);
        when(denied.getType()).thenReturn("MEALS");
        when(denied.getDescription()).thenReturn("Denied meal");
        when(denied.getSubmittedAt()).thenReturn(submittedAt);
        when(denied.getResolvedAt()).thenReturn(submittedAt);

        when(reimbursementRepository.findByStatus(ReimbursementStatus.APPROVED))
                .thenReturn(List.of(approved));

        when(reimbursementRepository.findByStatus(ReimbursementStatus.DENIED))
                .thenReturn(List.of(denied));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementHistory();

        assertEquals(2, result.size());
        assertEquals(101, result.get(0).getId());
        assertEquals(102, result.get(1).getId());

        verify(reimbursementRepository)
                .findByStatus(ReimbursementStatus.APPROVED);

        verify(reimbursementRepository)
                .findByStatus(ReimbursementStatus.DENIED);
    }

    // ---------------------------------------------------------
    // getReimbursements
    // ---------------------------------------------------------

    @Test
    void testGetReimbursementsWithStatusAndDepartment() {
        when(reimbursementRepository
                .findByStatusAndAuthor_Department_DepartmentId(
                        ReimbursementStatus.APPROVED, 10))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursements(
                        ReimbursementStatus.APPROVED, 10);

        assertEquals(1, result.size());

        verify(reimbursementRepository)
                .findByStatusAndAuthor_Department_DepartmentId(
                        ReimbursementStatus.APPROVED, 10);
    }

    @Test
    void testGetReimbursementsWithStatusOnly() {
        when(reimbursementRepository.findByStatus(
                ReimbursementStatus.APPROVED))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursements(
                        ReimbursementStatus.APPROVED, null);

        assertEquals(1, result.size());

        verify(reimbursementRepository)
                .findByStatus(ReimbursementStatus.APPROVED);
    }

    @Test
    void testGetReimbursementsWithDepartmentOnly() {
        when(reimbursementRepository
                .findByAuthor_Department_DepartmentId(10))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursements(null, 10);

        assertEquals(1, result.size());

        verify(reimbursementRepository)
                .findByAuthor_Department_DepartmentId(10);
    }

    @Test
    void testGetReimbursementsWithNoFilters() {
        when(reimbursementRepository.findAll())
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursements(null, null);

        assertEquals(1, result.size());

        verify(reimbursementRepository).findAll();
    }

    // ---------------------------------------------------------
    // getReimbursementsByStatus
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // getReimbursementsByDepartment
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // getReimbursementsByStatusAndDepartment
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // getReimbursementsByAuthor
    // ---------------------------------------------------------

    @Test
    void testGetReimbursementsByAuthor() {
        when(reimbursementRepository.findByAuthor(1))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementsByAuthor(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAuthorId());

        verify(reimbursementRepository).findByAuthor(1);
    }

    // ---------------------------------------------------------
    // getReimbursementsByAuthorAndStatus
    // ---------------------------------------------------------

    @Test
    void testGetReimbursementsByAuthorAndStatus() {
        when(reimbursementRepository.findByAuthorAndStatus(
                1, ReimbursementStatus.PENDING))
                .thenReturn(List.of(reimbursement));

        List<ReimbursementResponse> result =
                reimbursementService.getReimbursementsByAuthorAndStatus(
                        1, ReimbursementStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(ReimbursementStatus.PENDING,
                result.get(0).getStatus());

        verify(reimbursementRepository)
                .findByAuthorAndStatus(
                        1, ReimbursementStatus.PENDING);
    }

    // ---------------------------------------------------------
    // createReimbursement
    // ---------------------------------------------------------

    @Test
    void testCreateReimbursement() {
        CreateReimbursementReq request =
                mock(CreateReimbursementReq.class);

        when(request.getAmount()).thenReturn(300.00);
        when(request.getType()).thenReturn("TRAVEL");
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

    // ---------------------------------------------------------
    // updateReimbursement
    // ---------------------------------------------------------

    @Test
    void testUpdateReimbursement() {
        PatchReimbursementReq request =
                mock(PatchReimbursementReq.class);

        when(request.getAmount()).thenReturn(450.00);
        when(request.getType()).thenReturn("LODGING");
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

        verify(reimbursement).setAmount(450.00);
        verify(reimbursement).setType("LODGING");
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

    // ---------------------------------------------------------
    // deleteReimbursement
    // ---------------------------------------------------------

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

        reimbursementService.deleteReimbursement(100, "jane");

        verify(reimbursementRepository, never()).deleteById(100);
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

    // ---------------------------------------------------------
    // resolveReimbursement
    // ---------------------------------------------------------

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

        when(request.getStatus())
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

    // ---------------------------------------------------------
    // transformReimbursementToResponse(Reimbursement)
    // ---------------------------------------------------------

    @Test
    void testTransformReimbursementToResponse() {
        ReimbursementResponse result =
                ReimbursementServiceImpl
                        .transformReimbursementToResponse(reimbursement);

        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals(1, result.getAuthorId());
        assertNull(result.getResolverId());
        assertEquals(250.00, result.getAmount());
        assertEquals(ReimbursementStatus.PENDING, result.getStatus());
        assertEquals("TRAVEL", result.getType());
        assertEquals("Business trip", result.getDescription());
        assertEquals(submittedAt, result.getSubmittedAt());
        assertNull(result.getResolvedAt());
    }

    @Test
    void testTransformReimbursementToResponseWithResolver() {
        when(reimbursement.getResolver()).thenReturn(resolver);
        when(reimbursement.getResolvedAt()).thenReturn(submittedAt);

        ReimbursementResponse result =
                ReimbursementServiceImpl
                        .transformReimbursementToResponse(reimbursement);

        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals(1, result.getAuthorId());
        assertEquals(2, result.getResolverId());
        assertEquals(submittedAt, result.getResolvedAt());
    }

    // ---------------------------------------------------------
    // transformReimbursementToResponse(List)
    // ---------------------------------------------------------

    @Test
    void testTransformReimbursementListToResponseList() {
        Reimbursement secondReimbursement =
                mock(Reimbursement.class);

        when(secondReimbursement.getId()).thenReturn(200);
        when(secondReimbursement.getAuthor()).thenReturn(author);
        when(secondReimbursement.getResolver()).thenReturn(null);
        when(secondReimbursement.getAmount()).thenReturn(100.00);
        when(secondReimbursement.getStatus())
                .thenReturn(ReimbursementStatus.APPROVED);
        when(secondReimbursement.getType()).thenReturn("MEALS");
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

    @Test
    void testTransformEmptyReimbursementListToResponseList() {
        List<ReimbursementResponse> result =
                ReimbursementServiceImpl
                        .transformReimbursementToResponse(
                                Collections.emptyList()
                        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
```

The tests covered:

* Successful retrieval of all reimbursements
* Successful retrieval by ID
* `ReimbursementNotFoundException`
* Reimbursement history
* All `getReimbursements()` filter combinations
* Filtering by status
* Filtering by department
* Filtering by author
* Filtering by author and status
* Creating reimbursements
* `UserNotFoundException` during creation
* Updating reimbursements
* Update-related user and reimbursement exceptions
* Deleting reimbursements
* Preventing deletion by a non-author
* Delete-related exceptions
* Resolving reimbursements
* Resolution-related exceptions
* Single reimbursement transformation
* List transformation
* Empty list transformation

The assistant also recommended removing `@SpringBootTest` because the tests were intended to be unit tests rather than integration tests.

---

## Prompt 2 — Review and Finalize `ReimbursementServiceImplTest`

### User Prompt

The user supplied the reviewed/final version of `ReimbursementServiceImplTest`.

Important project-specific adjustments included:

```java
import com.revature.ERS2.models.ReimbursementType;
```

and:

```java
import java.math.BigDecimal;
```

The final test class therefore used:

```java
new BigDecimal("250.00")
```

instead of primitive decimal values and:

```java
ReimbursementType.TRAVEL
ReimbursementType.FOOD
ReimbursementType.LODGING
```

instead of strings.

The user then supplied the complete `ReimbursementController` implementation and requested:

> Generate a `ReimbursementControllerTest` class to test `ReimbursementController` class shown below. The test class should be similar in style to the final `ReimbursementServiceImpleTest` class shown previously.

### Relevant Controller Methods

The controller exposes:

```text
GET    /api/reimbursements/{id}
GET    /api/reimbursements/history
GET    /api/reimbursements
POST   /api/reimbursements
DELETE /api/reimbursements/{id}
PATCH  /api/reimbursements/{id}
PATCH  /api/reimbursements/{id}/status
```

It delegates operations to:

```java
private final ReimbursementService reimbursementService;
```

and uses:

```java
Authentication authentication
```

for username-dependent operations.

---

## Assistant Response — `ReimbursementControllerTest`

The assistant generated a JUnit 5 + Mockito unit test class using the same general style as the finalized service test.

The test class uses:

```java
@ExtendWith(MockitoExtension.class)
public class ReimbursementControllerTest {
```

with:

```java
@Mock
private ReimbursementService reimbursementService;

@Mock
private Authentication authentication;

@InjectMocks
private ReimbursementController reimbursementController;
```

A reusable `ReimbursementResponse` is initialized in `setUp()`:

```java
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
```

### Tests Generated

#### `testGetReimbursementById()`

Verifies:

* Controller returns the service response
* Correct reimbursement ID
* Correct author ID
* Correct amount
* Correct status
* Correct type
* Correct description
* Service is called with the expected ID

#### `testGetReimbursementHistory()`

Creates approved and denied responses and verifies:

* Two responses are returned
* Correct IDs
* Correct statuses
* Service method is invoked

#### `testGetReimbursementsWithStatusAndDepartment()`

Verifies:

```java
reimbursementService.getReimbursements(
    ReimbursementStatus.APPROVED, 10
);
```

and confirms HTTP 200.

#### `testGetReimbursementsWithStatusOnly()`

Verifies status filtering and HTTP 200.

#### `testGetReimbursementsWithDepartmentOnly()`

Verifies department filtering and HTTP 200.

#### `testGetReimbursementsWithNoFilters()`

Verifies that both parameters can be `null` and the controller still returns HTTP 200.

#### `testCreateReimbursement()`

Mocks:

```java
authentication.getName()
```

to return:

```text
john
```

Then verifies that the controller:

* Retrieves the username
* Passes the username and request to the service
* Returns HTTP 201
* Returns the expected response body

#### `testDeleteReimbursement()`

Verifies:

* Authentication username is retrieved
* Service receives the reimbursement ID and username
* Controller returns HTTP 204
* Response body is `null`

#### `testPatchReimbursement()`

Verifies:

* Authentication username
* Request forwarding
* Reimbursement ID forwarding
* Updated response
* HTTP 200
* Updated amount, type, and description

#### `testResolveReimbursement()`

Verifies:

* Authentication username
* Request forwarding
* Reimbursement ID forwarding
* Resolver ID
* Approved status
* HTTP 200

---

## Final Testing Approach

The resulting controller test follows the same philosophy as the finalized service test:

### Frameworks

* JUnit 5
* Mockito
* Spring's `ResponseEntity`
* Spring Security's `Authentication`

### Test Style

Pure unit tests are used rather than Spring MVC integration tests.

The controller's dependency is mocked:

```java
@Mock
private ReimbursementService reimbursementService;
```

The controller itself is created through:

```java
@InjectMocks
private ReimbursementController reimbursementController;
```

This means the tests focus exclusively on controller behavior and verify that:

1. HTTP response statuses are correct.
2. Response bodies are correct.
3. Authentication usernames are correctly extracted.
4. Service methods are called with the correct arguments.
5. Controller methods correctly delegate to the service layer.

### No `@SpringBootTest`

The controller test intentionally does not use:

```java
@SpringBootTest
```

because it is designed as a unit test rather than a full Spring application-context test.

---

## Resulting Project Test Structure

The conversation produced two complementary unit-test classes:

```text
src
└── test
    └── java
        └── com
            └── revature
                └── ERS2
                    ├── services
                    │   └── ReimbursementServiceImplTest.java
                    │
                    └── controllers
                        └── ReimbursementControllerTest.java
```

Both test classes use JUnit 5 and Mockito and isolate their respective classes from their dependencies.
