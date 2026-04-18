package com.example.collegedb.Controller;
import java.util.List;
import java.util.stream.Collectors;

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

import com.example.collegedb.Repository.FacultyScheduleRepository;
import com.example.collegedb.Response.FacultyScheduleResponse;
import com.example.collegedb.entity.FacultySchedule;


@RestController
@RequestMapping("/facultySchedule")
public class FacultyScheduleController {
    @Autowired
    private FacultyScheduleRepository facultyScheduleRepository;

    @GetMapping
    public List<FacultyScheduleResponse> getAllFacultySchedules() {
        return facultyScheduleRepository.findAll().stream()
            .map(fs -> new FacultyScheduleResponse(
                fs.getFacultyScheduleId(),
                fs.getFaculty().getFacultyId(),
                fs.getSubject().getSubjectId(),
                fs.getScheduleTime().toString(),
                fs.getClassroom()
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FacultyScheduleResponse getFacultyScheduleById(@PathVariable Long id) {
        FacultySchedule fs = facultyScheduleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty Schedule not found with id " + id));
        return new FacultyScheduleResponse(
            fs.getFacultyScheduleId(),
            fs.getFaculty().getFacultyId(),
            fs.getSubject().getSubjectId(),
            fs.getScheduleTime().toString(),
            fs.getClassroom()
        );
    }

    @PostMapping
    public FacultyScheduleResponse createFacultySchedule(@RequestBody FacultySchedule facultySchedule) {
        FacultySchedule saved = facultyScheduleRepository.save(facultySchedule);
        return new FacultyScheduleResponse(
            saved.getFacultyScheduleId(),
            saved.getFaculty().getFacultyId(),
            saved.getSubject().getSubjectId(),
            saved.getScheduleTime().toString(),
            saved.getClassroom()
        );
    }

    @PutMapping("/{id}")
    public FacultyScheduleResponse updateFacultySchedule(@PathVariable Long id, @RequestBody FacultySchedule facultyScheduleDetails) {
        FacultySchedule fs = facultyScheduleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty Schedule not found with id " + id));
        
        fs.setFaculty(facultyScheduleDetails.getFaculty());
        fs.setSubject(facultyScheduleDetails.getSubject());
        fs.setScheduleTime(facultyScheduleDetails.getScheduleTime());
        fs.setClassroom(facultyScheduleDetails.getClassroom());

        FacultySchedule updated = facultyScheduleRepository.save(fs);
        return new FacultyScheduleResponse(
            updated.getFacultyScheduleId(),
            updated.getFaculty().getFacultyId(),
            updated.getSubject().getSubjectId(),
            updated.getScheduleTime().toString(),
            updated.getClassroom()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteFacultySchedule(@PathVariable Long id) {
        facultyScheduleRepository.deleteById(id);
    }

    @PatchMapping("/{id}")
    public FacultyScheduleResponse patchFacultySchedule(@PathVariable Long id, @RequestBody FacultySchedule facultyScheduleDetails) {
        FacultySchedule fs = facultyScheduleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty Schedule not found with id " + id));

        if (facultyScheduleDetails.getFaculty() != null) {
            fs.setFaculty(facultyScheduleDetails.getFaculty());
        }
        if (facultyScheduleDetails.getSubject() != null) {
            fs.setSubject(facultyScheduleDetails.getSubject());
        }
        if (facultyScheduleDetails.getScheduleTime() != null) {
            fs.setScheduleTime(facultyScheduleDetails.getScheduleTime());
        }
        if (facultyScheduleDetails.getClassroom() != null) {
            fs.setClassroom(facultyScheduleDetails.getClassroom());
        }

        FacultySchedule updated = facultyScheduleRepository.save(fs);
        return new FacultyScheduleResponse(
            updated.getFacultyScheduleId(),
            updated.getFaculty().getFacultyId(),
            updated.getSubject().getSubjectId(),
            updated.getScheduleTime().toString(),
            updated.getClassroom()
        );
    }
}
