package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.DepartmentRepository;
import com.example.collegedb.Repository.FacultyRepository;
import com.example.collegedb.Repository.FacultyScheduleRepository;
import com.example.collegedb.Repository.SubjectRepository;
import com.example.collegedb.Response.FacultyScheduleResponse;
import com.example.collegedb.entity.Department;
import com.example.collegedb.entity.Faculty;
import com.example.collegedb.entity.FacultySchedule;
import com.example.collegedb.entity.Subject;

@Service
@Transactional
public class FacultyScheduleService {

    private final FacultyScheduleRepository facultyScheduleRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;

    public FacultyScheduleService(
        FacultyScheduleRepository facultyScheduleRepository,
        FacultyRepository facultyRepository,
        DepartmentRepository departmentRepository,
        SubjectRepository subjectRepository
    ) {
        this.facultyScheduleRepository = facultyScheduleRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional(readOnly = true)
    public List<FacultyScheduleResponse> getAllFacultySchedules() {
        return facultyScheduleRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FacultyScheduleResponse getFacultyScheduleById(Long id) {
        return toResponse(getExistingFacultySchedule(id));
    }

    public FacultyScheduleResponse createFacultySchedule(FacultySchedule facultySchedule) {
        facultySchedule.setFaculty(resolveFaculty(facultySchedule.getFaculty()));
        facultySchedule.setDepartment(resolveDepartment(facultySchedule.getDepartment()));
        facultySchedule.setSubject(resolveSubject(facultySchedule.getSubject()));
        return toResponse(facultyScheduleRepository.save(facultySchedule));
    }

    public FacultyScheduleResponse updateFacultySchedule(Long id, FacultySchedule facultyScheduleDetails) {
        FacultySchedule fs = getExistingFacultySchedule(id);
        fs.setFaculty(resolveFaculty(facultyScheduleDetails.getFaculty()));
        fs.setDepartment(resolveDepartment(facultyScheduleDetails.getDepartment()));
        fs.setSubject(resolveSubject(facultyScheduleDetails.getSubject()));
        fs.setScheduleTime(facultyScheduleDetails.getScheduleTime());
        fs.setClassroom(facultyScheduleDetails.getClassroom());
        return toResponse(facultyScheduleRepository.save(fs));
    }

    public void deleteFacultySchedule(Long id) {
        FacultySchedule fs = getExistingFacultySchedule(id);
        facultyScheduleRepository.delete(fs);
    }

    public FacultyScheduleResponse patchFacultySchedule(Long id, FacultySchedule facultyScheduleDetails) {
        FacultySchedule fs = getExistingFacultySchedule(id);
        if (facultyScheduleDetails.getFaculty() != null) {
            fs.setFaculty(resolveFaculty(facultyScheduleDetails.getFaculty()));
        }
        if (facultyScheduleDetails.getDepartment() != null) {
            fs.setDepartment(resolveDepartment(facultyScheduleDetails.getDepartment()));
        }
        if (facultyScheduleDetails.getSubject() != null) {
            fs.setSubject(resolveSubject(facultyScheduleDetails.getSubject()));
        }
        if (facultyScheduleDetails.getScheduleTime() != null) {
            fs.setScheduleTime(facultyScheduleDetails.getScheduleTime());
        }
        if (facultyScheduleDetails.getClassroom() != null) {
            fs.setClassroom(facultyScheduleDetails.getClassroom());
        }
        return toResponse(facultyScheduleRepository.save(fs));
    }

    private FacultySchedule getExistingFacultySchedule(Long id) {
        return facultyScheduleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Faculty Schedule not found with ID: " + id));
    }

    private Faculty resolveFaculty(Faculty faculty) {
        if (faculty == null || faculty.getFacultyId() == null) {
            return null;
        }
        Long facultyId = faculty.getFacultyId();
        return facultyRepository.findById(facultyId)
            .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + facultyId));
    }

    private Department resolveDepartment(Department department) {
        if (department == null || department.getDepartmentId() == null) {
            return null;
        }
        Long departmentId = department.getDepartmentId();
        return departmentRepository.findById(departmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
    }

    private Subject resolveSubject(Subject subject) {
        if (subject == null || subject.getSubjectId() == null) {
            return null;
        }
        Long subjectId = subject.getSubjectId();
        return subjectRepository.findById(subjectId)
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
    }

    private FacultyScheduleResponse toResponse(FacultySchedule fs) {
        return new FacultyScheduleResponse(
            fs.getFacultyScheduleId(),
            fs.getFaculty() != null ? fs.getFaculty().getFacultyId() : null,
            fs.getDepartment() != null ? fs.getDepartment().getDepartmentId() : null,
            fs.getDepartment() != null ? fs.getDepartment().getDepartmentName() : null,
            fs.getSubject() != null ? fs.getSubject().getSubjectId() : null,
            fs.getScheduleTime().toString(),
            fs.getClassroom()
        );
    }
}
