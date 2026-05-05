package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.DepartmentRepository;
import com.example.collegedb.Repository.FacultyRepository;
import com.example.collegedb.Repository.TimetableRepository;
import com.example.collegedb.Response.TimetableResponse;
import com.example.collegedb.entity.Department;
import com.example.collegedb.entity.Faculty;
import com.example.collegedb.entity.Timetable;

@Service
@Transactional
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    public TimetableService(
        TimetableRepository timetableRepository,
        DepartmentRepository departmentRepository,
        FacultyRepository facultyRepository
    ) {
        this.timetableRepository = timetableRepository;
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getAllTimetables() {
        return timetableRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TimetableResponse getTimetableById(Long id) {
        return toResponse(getExistingTimetable(id));
    }

    public TimetableResponse createTimetable(Timetable timetable) {
        timetable.setDepartment(resolveDepartment(timetable.getDepartment()));
        timetable.setFaculty(resolveFaculty(timetable.getFaculty()));
        return toResponse(timetableRepository.save(timetable));
    }

    public TimetableResponse updateTimetable(Long id, Timetable updatedTimetable) {
        Timetable existingTimetable = getExistingTimetable(id);
        existingTimetable.setSemester(updatedTimetable.getSemester());
        existingTimetable.setDay(updatedTimetable.getDay());
        existingTimetable.setTimeSlot(updatedTimetable.getTimeSlot());
        existingTimetable.setDepartment(resolveDepartment(updatedTimetable.getDepartment()));
        existingTimetable.setFaculty(resolveFaculty(updatedTimetable.getFaculty()));
        return toResponse(timetableRepository.save(existingTimetable));
    }

    public void deleteTimetable(Long id) {
        Timetable timetable = getExistingTimetable(id);
        timetableRepository.delete(timetable);
    }

    public TimetableResponse patchTimetable(Long id, Timetable updatedTimetable) {
        Timetable existingTimetable = getExistingTimetable(id);
        if (updatedTimetable.getSemester() != null) {
            existingTimetable.setSemester(updatedTimetable.getSemester());
        }
        if (updatedTimetable.getDay() != null) {
            existingTimetable.setDay(updatedTimetable.getDay());
        }
        if (updatedTimetable.getTimeSlot() != null) {
            existingTimetable.setTimeSlot(updatedTimetable.getTimeSlot());
        }
        if (updatedTimetable.getDepartment() != null) {
            existingTimetable.setDepartment(resolveDepartment(updatedTimetable.getDepartment()));
        }
        if (updatedTimetable.getFaculty() != null) {
            existingTimetable.setFaculty(resolveFaculty(updatedTimetable.getFaculty()));
        }
        return toResponse(timetableRepository.save(existingTimetable));
    }

    private Timetable getExistingTimetable(Long id) {
        return timetableRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Timetable not found with ID: " + id));
    }

    private Department resolveDepartment(Department department) {
        if (department == null || department.getDepartmentId() == null) {
            return null;
        }
        Long departmentId = department.getDepartmentId();
        return departmentRepository.findById(departmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
    }

    private Faculty resolveFaculty(Faculty faculty) {
        if (faculty == null || faculty.getFacultyId() == null) {
            return null;
        }
        Long facultyId = faculty.getFacultyId();
        return facultyRepository.findById(facultyId)
            .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + facultyId));
    }

    private TimetableResponse toResponse(Timetable timetable) {
        return new TimetableResponse(
            timetable.getTimeTableId(),
            timetable.getSemester(),
            timetable.getDay(),
            timetable.getTimeSlot(),
            timetable.getDepartment() != null ? timetable.getDepartment().getDepartmentName() : null,
            timetable.getFaculty() != null ? timetable.getFaculty().getFacultyName() : null
        );
    }
}
