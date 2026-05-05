package com.example.collegedb.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Response.CourseResponse;
import com.example.collegedb.Service.CourseService;
import com.example.collegedb.entity.Course;

@RestController
@RequestMapping("/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseResponse> getAllCourses() {
        logger.info("Fetching all courses...");
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable Long id) {
        logger.info("Fetching course with ID: {}", id);
        return courseService.getCourseById(id);
    }

    @PostMapping
    public CourseResponse createCourse(@RequestBody Course course) {
        logger.info("Creating new course: {}", course.getCourseName());
        return courseService.createCourse(course);
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        logger.info("Updating course with ID: {}", id);
        return courseService.updateCourse(id, courseDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        logger.info("Deleting course with ID: {}", id);
        courseService.deleteCourse(id);
    }

    @PatchMapping("/{id}")
    public CourseResponse patchCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        logger.info("Patching course with ID: {}", id);
        return courseService.patchCourse(id, courseDetails);
    }
}
