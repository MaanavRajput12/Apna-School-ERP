package com.example.collegedb.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.AttendanceRepository;
import com.example.collegedb.Response.AttendanceResponse;
import com.example.collegedb.entity.Attendance;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping
    public List<AttendanceResponse> getAll() {
        logger.info("Fetching all attendance records...");
        return attendanceRepository.findAll().stream()
            .map(s -> new AttendanceResponse(
                s.getAttendanceId(),
                s.getStudent() != null ? s.getStudent().getStudentId() : null,
                s.getDate(),
                s.getPresent(),
                s.getSubject() != null ? s.getSubject().getSubjectId() : null,
                s.getFaculty() != null ? s.getFaculty().getFacultyId() : null
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AttendanceResponse getById(@PathVariable Long id) {
        logger.info("Fetching attendance record with ID: {}", id);
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("EntityName not found with ID: " + id));
        logger.info("Attendance with ID: {} fetched successfully", id);
        return new AttendanceResponse(
            attendance.getAttendanceId(),
            attendance.getStudent() != null ? attendance.getStudent().getStudentId() : null,
            attendance.getDate(),
            attendance.getPresent(),
            attendance.getSubject() != null ? attendance.getSubject().getSubjectId() : null,
            attendance.getFaculty() != null ? attendance.getFaculty().getFacultyId() : null
        );
    }

    @PostMapping
    public AttendanceResponse create(@Valid @RequestBody Attendance attendance) {
        logger.info("Creating new attendance record for student ID: {}");
        Attendance saved = attendanceRepository.save(attendance);
        logger.debug("Attendance record created with ID: {}", saved.getAttendanceId());
        return new AttendanceResponse(
            saved.getAttendanceId(),
            saved.getStudent() != null ? saved.getStudent().getStudentId() : null,
            saved.getDate(),
            saved.getPresent(),
            saved.getSubject() != null ? saved.getSubject().getSubjectId() : null,
            saved.getFaculty() != null ? saved.getFaculty().getFacultyId() : null
        );
    }
    @PutMapping("/{id}")
    public AttendanceResponse update(@PathVariable Long id, @RequestBody Attendance entity) {
        logger.info("Updating attendance record with ID: {}", id);
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("EntityName not found with ID: " + id));
        logger.info("Attendance with ID: {} fetched successfully", id);
        logger.debug("Current attendance record: {}", attendance);
        attendance.setDate(entity.getDate());
        attendance.setPresent(entity.getPresent());
        attendance.setStudent(entity.getStudent());
        attendance.setSubject(entity.getSubject());
        attendance.setFaculty(entity.getFaculty());
            
        Attendance updated = attendanceRepository.save(attendance);
        logger.info("Successfully updated attendance record with ID: {}", id);
        return new AttendanceResponse(
            updated.getAttendanceId(),
            updated.getStudent() != null ? updated.getStudent().getStudentId() : null,
            updated.getDate(),
            updated.getPresent(),
            updated.getSubject() != null ? updated.getSubject().getSubjectId() : null,
            updated.getFaculty() != null ? updated.getFaculty().getFacultyId() : null
        );
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Deleting admin with ID: {}", id);
        attendanceRepository.deleteById(id);
        if (!attendanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendance not found with ID: " + id);
        }
        logger.info("Attendance with ID: {} deleted successfully", id);
    }
}
