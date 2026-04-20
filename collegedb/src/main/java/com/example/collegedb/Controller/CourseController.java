package com.example.collegedb.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.CourseRepository;
import com.example.collegedb.Response.CourseResponse;
import com.example.collegedb.entity.Course;


@RestController
@RequestMapping("/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public List<CourseResponse> getAllCourses() {
        logger.info("Fetching all courses...");
        return courseRepository.findAll().stream()
            .map(c -> new CourseResponse(
                c.getCourseId(),
                c.getCourseName(),
                c.getCredits(),
                c.getDepartment() != null ? c.getDepartment().getDepartmentId() : null,
                c.getDepartment() != null ? c.getDepartment().getDepartmentName() : null
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable Long id) {
        logger.info("Fetching course with ID: {}", id);
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
        logger.info("Course with ID: {} fetched successfully", id);
        return new CourseResponse(
            course.getCourseId(),
            course.getCourseName(),
            course.getCredits(),
            course.getDepartment() != null ? course.getDepartment().getDepartmentId() : null,
            course.getDepartment() != null ? course.getDepartment().getDepartmentName() : null
        );
    }

    @PostMapping
    public CourseResponse createCourse(@RequestBody Course course) {
        logger.info("Creating new course: {}", course.getCourseName());
        Course saved = courseRepository.save(course);
        logger.debug("Course created with ID: {}", saved.getCourseId());
        return new CourseResponse(
            saved.getCourseId(),
            saved.getCourseName(),
            saved.getCredits(),
            saved.getDepartment() != null ? saved.getDepartment().getDepartmentId() : null,
            saved.getDepartment() != null ? saved.getDepartment().getDepartmentName() : null
        );
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        logger.info("Updating course with ID: {}", id);
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));

        course.setCourseName(courseDetails.getCourseName());
        course.setCredits(courseDetails.getCredits());
        course.setDepartment(courseDetails.getDepartment());

        Course updated = courseRepository.save(course);
        logger.debug("Course updated with ID: {}", updated.getCourseId());
        return new CourseResponse(
            updated.getCourseId(),
            updated.getCourseName(),
            updated.getCredits(),
            updated.getDepartment() != null ? updated.getDepartment().getDepartmentId() : null,
            updated.getDepartment() != null ? updated.getDepartment().getDepartmentName() : null
        );
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        logger.info("Deleting course with ID: {}", id);
        courseRepository.deleteById(id);
        logger.debug("Course deleted with ID: {}", id);
    }

    @PatchMapping("/{id}")
    public CourseResponse patchCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        logger.info("Patching course with ID: {}", id);
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));

        if (courseDetails.getCourseName() != null) {
            course.setCourseName(courseDetails.getCourseName());
        }
        if (courseDetails.getCredits() != null) {
            course.setCredits(courseDetails.getCredits());
        }
        if (courseDetails.getDepartment() != null) {
            course.setDepartment(courseDetails.getDepartment());
        }

        Course updated = courseRepository.save(course);
        logger.debug("Course patched with ID: {}", updated.getCourseId());
        return new CourseResponse(
            updated.getCourseId(),
            updated.getCourseName(),
            updated.getCredits(),
            updated.getDepartment() != null ? updated.getDepartment().getDepartmentId() : null,
            updated.getDepartment() != null ? updated.getDepartment().getDepartmentName() : null
        );
    }
}
