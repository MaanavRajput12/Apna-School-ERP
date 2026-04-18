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
import com.example.collegedb.Repository.TimetableRepository;
import com.example.collegedb.Response.TimetableResponse;
import com.example.collegedb.entity.Timetable;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/timetables")

public class TimetableController {
    private static final Logger logger = LoggerFactory.getLogger(TimetableController.class);
    @Autowired
    private TimetableRepository timetableRepository;

    @GetMapping
    public List<TimetableResponse> getAll() {
        logger.info("Fetching all timetable records...");
        return timetableRepository.findAll().stream()
            .map(t -> new TimetableResponse(
                t.getTimeTableId(),
                t.getSemester(),
                t.getDay(),
                t.getTimeSlot(),
                t.getDepartment() != null ? t.getDepartment().getDepartmentName() : null,
                t.getFaculty() != null ? t.getFaculty().getFacultyName() : null
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TimetableResponse getById(@PathVariable Long id) {
        logger.info("Fetching timetable record with ID: {}", id);
        Timetable timetable = timetableRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Timetable record not found with ID: {}", id);
                return new ResourceNotFoundException("Timetable record not found with ID: " + id);
            });
        logger.info("Timetable record with ID: {} fetched successfully", id);
        return new TimetableResponse(
            timetable.getTimeTableId(),
            timetable.getSemester(),
            timetable.getDay(),
            timetable.getTimeSlot(),
            timetable.getDepartment() != null ? timetable.getDepartment().getDepartmentName() : null,
            timetable.getFaculty() != null ? timetable.getFaculty().getFacultyName() : null
        );
    }

    @PostMapping
    public TimetableResponse create(@Valid @RequestBody Timetable timetable) {
        logger.info("Creating new timetable record for semester: {}", timetable.getSemester());
        Timetable saved = timetableRepository.save(timetable);
        logger.debug("Timetable record created with ID: {}", saved.getTimeTableId());
        return new TimetableResponse(
            saved.getTimeTableId(),
            saved.getSemester(),
            saved.getDay(),
            saved.getTimeSlot(),
            saved.getDepartment() != null ? saved.getDepartment().getDepartmentName() : null,
            saved.getFaculty() != null ? saved.getFaculty().getFacultyName() : null
        );
    }

    @PutMapping("/{id}")
    public TimetableResponse update(@PathVariable Long id, @RequestBody Timetable updatedTimtable) {
        logger.info("Updating timetable record with ID: {}", id);

        Timetable existingTimetable = timetableRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Timetable record not found with ID: {}", id);
                return new ResourceNotFoundException("Timetable not found with ID: " + id);
            });

        existingTimetable.setSemester(updatedTimtable.getSemester());
        existingTimetable.setDay(updatedTimtable.getDay());
        existingTimetable.setTimeSlot(updatedTimtable.getTimeSlot());
        existingTimetable.setDepartment(updatedTimtable.getDepartment());
        existingTimetable.setFaculty(updatedTimtable.getFaculty());

        Timetable saved = timetableRepository.save(existingTimetable);
        logger.debug("Timetable record updated with ID: {}", saved.getTimeTableId());
        return new TimetableResponse(
            saved.getTimeTableId(),
            saved.getSemester(),
            saved.getDay(),
            saved.getTimeSlot(),
            saved.getDepartment() != null ? saved.getDepartment().getDepartmentName() : null,
            saved.getFaculty() != null ? saved.getFaculty().getFacultyName() : null
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete timetable record with ID: {}", id);
        Timetable timetable = timetableRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Timetable record not found with ID: " + id));
        timetableRepository.delete(timetable);
        logger.info("Timetable record deleted successfully with ID: {}", id);
    }    
    
    @PatchMapping("/{id}")
    public TimetableResponse patch(@PathVariable Long id, @RequestBody Timetable updatedTimetable) {
        logger.info("Patching timetable record with ID: {}", id);

        Timetable existingTimetable = timetableRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Timetable record not found with ID: {}", id);
                return new ResourceNotFoundException("Timetable not found with ID: " + id);
            });

        if (updatedTimetable.getSemester() != null) {
            existingTimetable.setSemester(updatedTimetable.getSemester());
        }
        if (updatedTimetable.getDay() != null) {
            existingTimetable.setDay(updatedTimetable.getDay());
        }
        if (updatedTimetable.getTimeSlot() != null) {
            existingTimetable.setTimeSlot(updatedTimetable.getTimeSlot());
        }
        if (updatedTimetable.getDepartment() != null) {
            existingTimetable.setDepartment(updatedTimetable.getDepartment());
        }
        if (updatedTimetable.getFaculty() != null) {
            existingTimetable.setFaculty(updatedTimetable.getFaculty());
        }

        Timetable saved = timetableRepository.save(existingTimetable);
        logger.debug("Timetable record patched with ID: {}", saved.getTimeTableId());
        return new TimetableResponse(
            saved.getTimeTableId(),
            saved.getSemester(),
            saved.getDay(),
            saved.getTimeSlot(),
            saved.getDepartment() != null ? saved.getDepartment().getDepartmentName() : null,
            saved.getFaculty() != null ? saved.getFaculty().getFacultyName() : null
        );
    }
}
