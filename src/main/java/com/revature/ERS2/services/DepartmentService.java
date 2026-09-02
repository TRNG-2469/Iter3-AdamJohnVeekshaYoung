package com.revature.ERS2.services;

import com.revature.ERS2.dtos.responses.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    public List<DepartmentResponse> getAllDepartments();

}
