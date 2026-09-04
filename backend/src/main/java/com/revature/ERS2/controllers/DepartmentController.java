package com.revature.ERS2.controllers;

import com.revature.ERS2.dtos.responses.DepartmentResponse;
import com.revature.ERS2.services.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DepartmentController {

    private final DepartmentService ds;

    public DepartmentController(DepartmentService ds) {
        this.ds = ds;
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(ds.getAllDepartments());
    }


}
