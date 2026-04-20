package com.example.collegedb.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ConflictException;
import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.CourseRepository;
import com.example.collegedb.Repository.DepartmentRepository;
import com.example.collegedb.Repository.FeesRepository;
import com.example.collegedb.Repository.StudentRepository;
import com.example.collegedb.Repository.UsersRepository;
import com.example.collegedb.Response.StudentResponse;
import com.example.collegedb.dto.student.StudentCreateRequest;
import com.example.collegedb.dto.student.StudentPatchRequest;
import com.example.collegedb.dto.student.StudentUpdateRequest;
import com.example.collegedb.entity.Course;
import com.example.collegedb.entity.Department;
import com.example.collegedb.entity.Fees;
import com.example.collegedb.entity.Student;
import com.example.collegedb.entity.Users;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final FeesRepository feesRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(
        StudentRepository studentRepository,
        CourseRepository courseRepository,
        DepartmentRepository departmentRepository,
        FeesRepository feesRepository,
        UsersRepository usersRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.feesRepository = feesRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        return toResponse(getExistingStudent(id));
    }

    public StudentResponse createStudent(StudentCreateRequest request) {
        Student student = new Student();
        apply(student, request);
        return toResponse(studentRepository.save(student));
    }

    public StudentResponse updateStudent(Long id, StudentUpdateRequest request) {
        Student student = getExistingStudent(id);
        apply(student, request);
        return toResponse(studentRepository.save(student));
    }

    public StudentResponse patchStudent(Long id, StudentPatchRequest request) {
        Student student = getExistingStudent(id);

        if (request.getName() != null) {
            student.setName(request.getName().trim());
        }
        if (request.getEmail() != null) {
            student.setEmail(request.getEmail().trim());
        }
        if (request.getDob() != null) {
            student.setDob(request.getDob());
        }
        if (request.getGender() != null) {
            student.setGender(request.getGender().trim());
        }
        if (request.getRollNo() != null) {
            student.setRollNo(request.getRollNo().trim());
        }
        if (request.getPhoneNo() != null) {
            student.setPhoneNo(request.getPhoneNo());
        }
        if (request.getAddress() != null) {
            student.setAddress(request.getAddress().trim());
        }
        if (request.getSemester() != null) {
            student.setSemester(request.getSemester().trim());
        }
        if (request.getFeesId() != null) {
            student.setFees(getFees(request.getFeesId()));
        }
        if (request.getCourseId() != null) {
            student.setCourse(getCourse(request.getCourseId()));
        }
        if (request.getDepartmentId() != null) {
            student.setDepartment(getDepartment(request.getDepartmentId()));
        }
        if (request.getUserId() != null) {
            student.setUser(getUser(request.getUserId()));
        }
        if (request.getLoginPassword() != null && !request.getLoginPassword().trim().isEmpty()) {
            student.setUser(upsertStudentUser(student.getUser(), student.getEmail(), request.getLoginPassword()));
        }

        return toResponse(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
    Student student = getExistingStudent(id);
    student.setActive(false);
    studentRepository.save(student);
}

    private void apply(Student student, StudentCreateRequest request) {
        student.setName(request.getName().trim());
        student.setEmail(request.getEmail().trim());
        student.setDob(request.getDob());
        student.setGender(request.getGender().trim());
        student.setRollNo(request.getRollNo().trim());
        student.setPhoneNo(request.getPhoneNo());
        student.setAddress(request.getAddress().trim());
        student.setSemester(request.getSemester().trim());
        student.setFees(getOptionalFees(request.getFeesId()));
        student.setCourse(getOptionalCourse(request.getCourseId()));
        student.setDepartment(getOptionalDepartment(request.getDepartmentId()));
        student.setUser(getOptionalUser(request.getUserId()));
        if (request.getLoginPassword() != null && !request.getLoginPassword().trim().isEmpty()) {
            student.setUser(upsertStudentUser(student.getUser(), student.getEmail(), request.getLoginPassword()));
        } else if (student.getUser() != null) {
            student.getUser().setUsername(student.getEmail().trim());
            student.getUser().setRole("STUDENT");
        }
    }

    private void apply(Student student, StudentUpdateRequest request) {
        student.setName(request.getName().trim());
        student.setEmail(request.getEmail().trim());
        student.setDob(request.getDob());
        student.setGender(request.getGender().trim());
        student.setRollNo(request.getRollNo().trim());
        student.setPhoneNo(request.getPhoneNo());
        student.setAddress(request.getAddress().trim());
        student.setSemester(request.getSemester().trim());
        student.setFees(getOptionalFees(request.getFeesId()));
        student.setCourse(getOptionalCourse(request.getCourseId()));
        student.setDepartment(getOptionalDepartment(request.getDepartmentId()));
        student.setUser(getOptionalUser(request.getUserId()));
        if (request.getLoginPassword() != null && !request.getLoginPassword().trim().isEmpty()) {
            student.setUser(upsertStudentUser(student.getUser(), student.getEmail(), request.getLoginPassword()));
        } else if (student.getUser() != null) {
            student.getUser().setUsername(student.getEmail().trim());
            student.getUser().setRole("STUDENT");
        }
    }

    private Student getExistingStudent(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    private Course getOptionalCourse(Long id) {
        return id == null ? null : getCourse(id);
    }

    private Department getOptionalDepartment(Long id) {
        return id == null ? null : getDepartment(id);
    }

    private Fees getOptionalFees(Long id) {
        return id == null ? null : getFees(id);
    }

    private Users getOptionalUser(Long id) {
        return id == null ? null : getUser(id);
    }

    private Course getCourse(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
    }

    private Department getDepartment(Long id) {
        return departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    private Fees getFees(Long id) {
        return feesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fees not found with ID: " + id));
    }

    private Users getUser(Long id) {
        return usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private Users upsertStudentUser(Users existingUser, String email, String rawPassword) {
        String normalizedUsername = email.trim();
        usersRepository.findByUsername(normalizedUsername)
            .filter(foundUser -> existingUser == null || !foundUser.getUserId().equals(existingUser.getUserId()))
            .ifPresent(foundUser -> {
                throw new ConflictException("Username already exists: " + normalizedUsername);
            });

        Users user = existingUser != null ? existingUser : new Users();
        user.setUsername(normalizedUsername);
        user.setPassword(passwordEncoder.encode(rawPassword.trim()));
        user.setRole("STUDENT");
        return usersRepository.save(user);
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
            student.getStudentId(),
            student.getName(),
            student.getEmail(),
            student.getDob(),
            student.getGender(),
            student.getRollNo(),
            student.getPhoneNo(),
            student.getAddress(),
            student.getSemester(),
            student.getDepartment() != null ? student.getDepartment().getDepartmentName() : null,
            student.getFees() != null ? student.getFees().getFeesStatus() : null,
            student.getFees() != null ? student.getFees().getFeesId() : null,
            student.getCourse() != null ? student.getCourse().getCourseName() : null,
            student.getUser() != null ? student.getUser().getUserId() : null,
            student.getActive()  
        );
    }
}
