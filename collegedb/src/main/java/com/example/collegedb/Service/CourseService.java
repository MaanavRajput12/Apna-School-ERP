package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.CourseRepository;
import com.example.collegedb.Response.CourseResponse;
import com.example.collegedb.entity.Course;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        return toResponse(getExistingCourse(id));
    }

    public CourseResponse createCourse(Course course) {
        return toResponse(courseRepository.save(course));
    }

    public CourseResponse updateCourse(Long id, Course courseDetails) {
        Course course = getExistingCourse(id);
        course.setCourseName(courseDetails.getCourseName());
        course.setCredits(courseDetails.getCredits());
        course.setDepartment(courseDetails.getDepartment());
        return toResponse(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        Course course = getExistingCourse(id);
        courseRepository.delete(course);
    }

    public CourseResponse patchCourse(Long id, Course courseDetails) {
        Course course = getExistingCourse(id);
        if (courseDetails.getCourseName() != null) {
            course.setCourseName(courseDetails.getCourseName());
        }
        if (courseDetails.getCredits() != null) {
            course.setCredits(courseDetails.getCredits());
        }
        if (courseDetails.getDepartment() != null) {
            course.setDepartment(courseDetails.getDepartment());
        }
        return toResponse(courseRepository.save(course));
    }

    private Course getExistingCourse(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
    }

    private CourseResponse toResponse(Course course) {
        return new CourseResponse(
            course.getCourseId(),
            course.getCourseName(),
            course.getCredits(),
            course.getDepartment() != null ? course.getDepartment().getDepartmentId() : null,
            course.getDepartment() != null ? course.getDepartment().getDepartmentName() : null
        );
    }
}
