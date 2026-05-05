package com.example.collegedb.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Response.FacultyScheduleResponse;
import com.example.collegedb.Service.FacultyScheduleService;
import com.example.collegedb.entity.FacultySchedule;

@RestController
@RequestMapping("/facultySchedule")
public class FacultyScheduleController {

    private final FacultyScheduleService facultyScheduleService;

    public FacultyScheduleController(FacultyScheduleService facultyScheduleService) {
        this.facultyScheduleService = facultyScheduleService;
    }

    @GetMapping
    public List<FacultyScheduleResponse> getAllFacultySchedules() {
        return facultyScheduleService.getAllFacultySchedules();
    }

    @GetMapping("/{id}")
    public FacultyScheduleResponse getFacultyScheduleById(@PathVariable Long id) {
        return facultyScheduleService.getFacultyScheduleById(id);
    }

    @PostMapping
    public FacultyScheduleResponse createFacultySchedule(@RequestBody FacultySchedule facultySchedule) {
        return facultyScheduleService.createFacultySchedule(facultySchedule);
    }

    @PutMapping("/{id}")
    public FacultyScheduleResponse updateFacultySchedule(@PathVariable Long id, @RequestBody FacultySchedule facultyScheduleDetails) {
        return facultyScheduleService.updateFacultySchedule(id, facultyScheduleDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteFacultySchedule(@PathVariable Long id) {
        facultyScheduleService.deleteFacultySchedule(id);
    }

    @PatchMapping("/{id}")
    public FacultyScheduleResponse patchFacultySchedule(@PathVariable Long id, @RequestBody FacultySchedule facultyScheduleDetails) {
        return facultyScheduleService.patchFacultySchedule(id, facultyScheduleDetails);
    }
}
