package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.DepartmentRepository;
import com.example.collegedb.Response.DepartmentResponse;
import com.example.collegedb.entity.Department;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        return toResponse(getExistingDepartment(id));
    }

    public DepartmentResponse createDepartment(Department department) {
        return toResponse(departmentRepository.save(department));
    }

    public DepartmentResponse updateDepartment(Long id, Department entity) {
        Department department = getExistingDepartment(id);
        department.setDepartmentName(entity.getDepartmentName());
        department.setNumberOfStudents(entity.getNumberOfStudents());
        department.setNumberOfFaculties(entity.getNumberOfFaculties());
        return toResponse(departmentRepository.save(department));
    }

    public void deleteDepartment(Long id) {
        Department department = getExistingDepartment(id);
        departmentRepository.delete(department);
    }

    public DepartmentResponse patchDepartment(Long id, Department entity) {
        Department department = getExistingDepartment(id);
        if (entity.getDepartmentName() != null) {
            department.setDepartmentName(entity.getDepartmentName());
        }
        if (entity.getNumberOfStudents() != null) {
            department.setNumberOfStudents(entity.getNumberOfStudents());
        }
        if (entity.getNumberOfFaculties() != null) {
            department.setNumberOfFaculties(entity.getNumberOfFaculties());
        }
        return toResponse(departmentRepository.save(department));
    }

    private Department getExistingDepartment(Long id) {
        return departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    private DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
            department.getDepartmentId(),
            department.getDepartmentName(),
            department.getNumberOfStudents(),
            department.getNumberOfFaculties()
        );
    }
}
