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

import com.example.collegedb.Response.TimetableResponse;
import com.example.collegedb.Service.TimetableService;
import com.example.collegedb.entity.Timetable;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/timetables")
public class TimetableController {

    private static final Logger logger = LoggerFactory.getLogger(TimetableController.class);
    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public List<TimetableResponse> getAll() {
        logger.info("Fetching all timetable records...");
        return timetableService.getAllTimetables();
    }

    @GetMapping("/{id}")
    public TimetableResponse getById(@PathVariable Long id) {
        logger.info("Fetching timetable record with ID: {}", id);
        return timetableService.getTimetableById(id);
    }

    @PostMapping
    public TimetableResponse create(@Valid @RequestBody Timetable timetable) {
        logger.info("Creating new timetable record for semester: {}", timetable.getSemester());
        return timetableService.createTimetable(timetable);
    }

    @PutMapping("/{id}")
    public TimetableResponse update(@PathVariable Long id, @RequestBody Timetable updatedTimetable) {
        logger.info("Updating timetable record with ID: {}", id);
        return timetableService.updateTimetable(id, updatedTimetable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete timetable record with ID: {}", id);
        timetableService.deleteTimetable(id);
    }

    @PatchMapping("/{id}")
    public TimetableResponse patch(@PathVariable Long id, @RequestBody Timetable updatedTimetable) {
        logger.info("Patching timetable record with ID: {}", id);
        return timetableService.patchTimetable(id, updatedTimetable);
    }
}
