package com.revature.ERS2.services;

import com.revature.ERS2.dtos.responses.DepartmentResponse;
import com.revature.ERS2.models.Department;
import com.revature.ERS2.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository dp;

    public DepartmentServiceImpl(DepartmentRepository dp) {
        this.dp = dp;
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return transformDepartmentToResponse(dp.findAll());
    }

    public static DepartmentResponse transformDepartmentToResponse(Department d) {
        return new DepartmentResponse(d.getDepartmentId(), d.getDepartmentName());
    }

    public static List<DepartmentResponse> transformDepartmentToResponse(List<Department> dList) {
        List<DepartmentResponse> listDepartments = new ArrayList<>();

        for (Department d : dList) {
            listDepartments.add(transformDepartmentToResponse(d));
        }

        return listDepartments;
    }

}
