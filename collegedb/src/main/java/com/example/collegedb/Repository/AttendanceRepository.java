package com.example.collegedb.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.collegedb.entity.Attendance;
import com.example.collegedb.entity.enums.AttendanceStatus;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentStudentId(Long studentId);

    List<Attendance> findByDateAndStudentDepartmentDepartmentNameIgnoreCase(LocalDate date, String department);

    boolean existsByStudentStudentIdAndDateAndSubjectSubjectId(Long studentId, LocalDate date, Long subjectId);

    long countByStudentStudentId(Long studentId);

    long countByStudentStudentIdAndStatus(Long studentId, AttendanceStatus status);

    List<Attendance> findByStudentStudentIdAndSubjectIsNotNull(Long studentId);

    @Query("""
        select a
        from Attendance a
        join a.student s
        join s.department d
        where a.date = :date and lower(d.departmentName) = lower(:department)
    """)
    List<Attendance> findByDateAndDepartment(@Param("date") LocalDate date, @Param("department") String department);
}
