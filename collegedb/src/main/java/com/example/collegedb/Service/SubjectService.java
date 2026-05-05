package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.DepartmentRepository;
import com.example.collegedb.Repository.FacultyRepository;
import com.example.collegedb.Repository.SubjectRepository;
import com.example.collegedb.Response.SubjectResponse;
import com.example.collegedb.entity.Department;
import com.example.collegedb.entity.Faculty;
import com.example.collegedb.entity.Subject;

@Service
@Transactional
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    public SubjectService(
        SubjectRepository subjectRepository,
        DepartmentRepository departmentRepository,
        FacultyRepository facultyRepository
    ) {
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SubjectResponse getSubjectById(Long id) {
        return toResponse(getExistingSubject(id));
    }

    public SubjectResponse createSubject(Subject subject) {
        subject.setDepartment(resolveDepartment(subject));
        subject.setFaculty(resolveFaculty(subject));
        subject.setActive(true);
        return toResponse(subjectRepository.save(subject));
    }

    public SubjectResponse updateSubject(Long id, Subject updatedData) {
        Subject subject = getExistingSubject(id);
        subject.setName(updatedData.getName());
        subject.setSyllabus(updatedData.getSyllabus());
        subject.setDepartment(resolveDepartment(updatedData));
        subject.setFaculty(resolveFaculty(updatedData));
        return toResponse(subjectRepository.save(subject));
    }

    public void deleteSubject(Long id) {
        Subject subject = getExistingSubject(id);
        subject.setActive(false);
        subjectRepository.save(subject);
    }

    public SubjectResponse patchSubject(Long id, Subject updatedData) {
        Subject subject = getExistingSubject(id);
        if (updatedData.getName() != null) {
            subject.setName(updatedData.getName());
        }
        if (updatedData.getSyllabus() != null) {
            subject.setSyllabus(updatedData.getSyllabus());
        }
        if (updatedData.getDepartment() != null) {
            subject.setDepartment(resolveDepartment(updatedData));
        }
        if (updatedData.getFaculty() != null) {
            subject.setFaculty(resolveFaculty(updatedData));
        }
        return toResponse(subjectRepository.save(subject));
    }

    private Subject getExistingSubject(Long id) {
        return subjectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
    }

    private Department resolveDepartment(Subject subject) {
        if (subject.getDepartment() == null || subject.getDepartment().getDepartmentId() == null) {
            return null;
        }
        Long departmentId = subject.getDepartment().getDepartmentId();
        return departmentRepository.findById(departmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
    }

    private Faculty resolveFaculty(Subject subject) {
        if (subject.getFaculty() == null || subject.getFaculty().getFacultyId() == null) {
            return null;
        }
        Long facultyId = subject.getFaculty().getFacultyId();
        return facultyRepository.findById(facultyId)
            .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + facultyId));
    }

    private SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(
            subject.getSubjectId(),
            subject.getName(),
            subject.getSyllabus(),
            subject.getFaculty() != null ? subject.getFaculty().getFacultyId() : null,
            subject.getDepartment() != null ? subject.getDepartment().getDepartmentName() : null,
            subject.getFaculty() != null ? subject.getFaculty().getFacultyName() : null,
            subject.isActive()
        );
    }
}
