package com.example.collegedb.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Response.AttendancePercentageResponse;
import com.example.collegedb.Response.AttendanceResponse;
import com.example.collegedb.Response.SubjectAttendancePercentageResponse;
import com.example.collegedb.Service.AttendanceService;
import com.example.collegedb.dto.attendance.AttendanceRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/attendance", "/api/attendance"})
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public List<AttendanceResponse> getAll() {
        logger.info("Fetching all attendance records...");
        return attendanceService.getAllAttendance();
    }

    @GetMapping("/{id}")
    public AttendanceResponse getById(@PathVariable Long id) {
        logger.info("Fetching attendance record with ID: {}", id);
        return attendanceService.getAttendanceById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<AttendanceResponse> getByStudentId(@PathVariable Long studentId) {
        logger.info("Fetching attendance records for student ID: {}", studentId);
        return attendanceService.getAttendanceByStudentId(studentId);
    }

    @PostMapping("/mark")
    public List<AttendanceResponse> markAttendance(@Valid @RequestBody List<AttendanceRequestDTO> requests) {
        logger.info("Marking attendance for {} students", requests.size());
        return attendanceService.markAttendance(requests);
    }

    @GetMapping("/percentage/{studentId}")
    public AttendancePercentageResponse getAttendancePercentage(@PathVariable Long studentId) {
        logger.info("Calculating attendance percentage for student ID: {}", studentId);
        return attendanceService.getAttendancePercentage(studentId);
    }

    @GetMapping("/percentage/{studentId}/subjects")
    public List<SubjectAttendancePercentageResponse> getSubjectWiseAttendancePercentage(@PathVariable Long studentId) {
        logger.info("Calculating subject-wise attendance percentage for student ID: {}", studentId);
        return attendanceService.getSubjectWiseAttendancePercentage(studentId);
    }
}
