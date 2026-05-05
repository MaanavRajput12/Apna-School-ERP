package com.example.collegedb.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ConflictException;
import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.AttendanceRepository;
import com.example.collegedb.Repository.StudentRepository;
import com.example.collegedb.Repository.SubjectRepository;
import com.example.collegedb.Response.AttendancePercentageResponse;
import com.example.collegedb.Response.AttendanceResponse;
import com.example.collegedb.Response.SubjectAttendancePercentageResponse;
import com.example.collegedb.dto.attendance.AttendanceRequestDTO;
import com.example.collegedb.entity.Attendance;
import com.example.collegedb.entity.Student;
import com.example.collegedb.entity.Subject;
import com.example.collegedb.entity.enums.AttendanceStatus;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public AttendanceService(
        AttendanceRepository attendanceRepository,
        StudentRepository studentRepository,
        SubjectRepository subjectRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendance() {
        return attendanceRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public AttendanceResponse getAttendanceById(Long id) {
        return toResponse(getExistingAttendance(id));
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByStudentId(Long studentId) {
        return attendanceRepository.findByStudentStudentId(studentId).stream()
            .map(this::toResponse)
            .toList();
    }

    public List<AttendanceResponse> markAttendance(List<AttendanceRequestDTO> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Attendance request list cannot be empty");
        }

        List<Attendance> savedRecords = requests.stream()
            .map(this::createAttendanceRecord)
            .map(attendanceRepository::save)
            .toList();

        return savedRecords.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AttendancePercentageResponse getAttendancePercentage(Long studentId) {
        getExistingStudent(studentId);

        long totalClasses = attendanceRepository.countByStudentStudentId(studentId);
        long presentCount = attendanceRepository.countByStudentStudentIdAndStatus(studentId, AttendanceStatus.PRESENT);

        double percentage = totalClasses == 0
            ? 0.0
            : Math.round(((double) presentCount / totalClasses) * 10000.0) / 100.0;

        return new AttendancePercentageResponse(studentId, percentage);
    }

    @Transactional(readOnly = true)
    public List<SubjectAttendancePercentageResponse> getSubjectWiseAttendancePercentage(Long studentId) {
        getExistingStudent(studentId);

        Map<Long, List<Attendance>> bySubject = attendanceRepository.findByStudentStudentIdAndSubjectIsNotNull(studentId).stream()
            .filter(attendance -> attendance.getSubject() != null && attendance.getSubject().getSubjectId() != null)
            .collect(Collectors.groupingBy(attendance -> attendance.getSubject().getSubjectId()));

        return bySubject.values().stream()
            .map(this::toSubjectAttendancePercentageResponse)
            .sorted((left, right) -> left.getSubjectName().compareToIgnoreCase(right.getSubjectName()))
            .toList();
    }

    private Attendance createAttendanceRecord(AttendanceRequestDTO request) {
        if (request.getSubjectId() == null) {
            throw new IllegalArgumentException("Subject is required when marking attendance");
        }

        if (attendanceRepository.existsByStudentStudentIdAndDateAndSubjectSubjectId(
            request.getStudentId(),
            request.getDate(),
            request.getSubjectId()
        )) {
            throw new ConflictException(
                "Attendance already marked for student ID " + request.getStudentId()
                    + " on " + request.getDate()
                    + " for subject ID " + request.getSubjectId()
            );
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(getExistingStudent(request.getStudentId()));
        attendance.setDate(request.getDate());
        attendance.setStatus(request.getStatus());
        attendance.setSubject(getOptionalSubject(request.getSubjectId()));
        return attendance;
    }

    private Attendance getExistingAttendance(Long id) {
        return attendanceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));
    }

    private Student getExistingStudent(Long studentId) {
        return studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
    }

    private Subject getOptionalSubject(Long subjectId) {
        if (subjectId == null) {
            return null;
        }
        return subjectRepository.findById(subjectId)
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
    }

    private AttendanceResponse toResponse(Attendance attendance) {
        return new AttendanceResponse(
            attendance.getId(),
            attendance.getStudent() != null ? attendance.getStudent().getStudentId() : null,
            attendance.getDate(),
            attendance.getStatus(),
            attendance.getSubject() != null ? attendance.getSubject().getSubjectId() : null
        );
    }

    private SubjectAttendancePercentageResponse toSubjectAttendancePercentageResponse(List<Attendance> subjectAttendance) {
        Attendance firstRecord = subjectAttendance.getFirst();
        long totalClasses = subjectAttendance.size();
        long presentCount = subjectAttendance.stream()
            .filter(attendance -> attendance.getStatus() == AttendanceStatus.PRESENT)
            .count();
        double percentage = totalClasses == 0
            ? 0.0
            : Math.round(((double) presentCount / totalClasses) * 10000.0) / 100.0;

        return new SubjectAttendancePercentageResponse(
            firstRecord.getSubject().getSubjectId(),
            firstRecord.getSubject().getName(),
            presentCount,
            totalClasses,
            percentage
        );
    }
}
