package com.example.CollegeDB.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.CourseRepository;
import com.example.collegedb.Repository.DepartmentRepository;
import com.example.collegedb.Repository.FeesRepository;
import com.example.collegedb.Repository.StudentRepository;
import com.example.collegedb.Repository.UsersRepository;
import com.example.collegedb.Service.StudentService;
import com.example.collegedb.dto.student.StudentCreateRequest;
import com.example.collegedb.dto.student.StudentPatchRequest;
import com.example.collegedb.entity.Course;
import com.example.collegedb.entity.Department;
import com.example.collegedb.entity.Fees;
import com.example.collegedb.entity.Student;
import com.example.collegedb.entity.Users;

class StudentServiceTest {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private DepartmentRepository departmentRepository;
    private FeesRepository feesRepository;
    private UsersRepository usersRepository;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository = org.mockito.Mockito.mock(StudentRepository.class);
        courseRepository = org.mockito.Mockito.mock(CourseRepository.class);
        departmentRepository = org.mockito.Mockito.mock(DepartmentRepository.class);
        feesRepository = org.mockito.Mockito.mock(FeesRepository.class);
        usersRepository = org.mockito.Mockito.mock(UsersRepository.class);
        studentService = new StudentService(
            studentRepository,
            courseRepository,
            departmentRepository,
            feesRepository,
            usersRepository
        );
    }

    @Test
    void createStudentMapsRelatedEntities() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setDob(LocalDate.of(2003, 1, 5));
        request.setGender("Female");
        request.setRollNo("R-101");
        request.setPhoneNo(9999999999L);
        request.setAddress("Hostel Block A");
        request.setSemester("6");
        request.setCourseId(1L);
        request.setDepartmentId(2L);
        request.setFeesId(3L);
        request.setUserId(4L);

        Course course = new Course();
        course.setCourseId(1L);
        course.setCourseName("BSc CS");

        Department department = new Department();
        department.setDepartmentId(2L);
        department.setDepartmentName("Computer Science");

        Fees fees = new Fees();
        fees.setFeesId(3L);
        fees.setFeesStatus("PAID");

        Users user = new Users();
        user.setUserId(4L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(department));
        when(feesRepository.findById(3L)).thenReturn(Optional.of(fees));
        when(usersRepository.findById(4L)).thenReturn(Optional.of(user));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0);
            saved.setStudentId(10L);
            return saved;
        });

        var response = studentService.createStudent(request);

        assertEquals(10L, response.getStudentId());
        assertEquals("Alice", response.getName());
        assertEquals("Computer Science", response.getDepartmentName());
        assertEquals("BSc CS", response.getCourseName());
        assertEquals("PAID", response.getFeesStatus());
        assertEquals("4", response.getUserId());
    }

    @Test
    void patchStudentUpdatesSelectedFields() {
        Student existing = new Student();
        existing.setStudentId(7L);
        existing.setName("Old Name");
        existing.setSemester("4");

        Course course = new Course();
        course.setCourseId(11L);
        course.setCourseName("MBA");

        StudentPatchRequest request = new StudentPatchRequest();
        request.setName("New Name");
        request.setSemester("5");
        request.setCourseId(11L);

        when(studentRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(courseRepository.findById(11L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = studentService.patchStudent(7L, request);

        assertEquals("New Name", response.getName());
        assertEquals("5", response.getSemester());
        assertEquals("MBA", response.getCourseName());
    }

    @Test
    void createStudentThrowsWhenRelatedCourseMissing() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setDob(LocalDate.of(2003, 1, 5));
        request.setGender("Female");
        request.setRollNo("R-101");
        request.setPhoneNo(9999999999L);
        request.setAddress("Hostel Block A");
        request.setSemester("6");
        request.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.createStudent(request));
        verify(studentRepository, never()).save(any(Student.class));
    }
}
