import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Faculty, FacultySchedule, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { FacultySessionService } from '../../services/faculty-session.service';

@Component({
  selector: 'app-faculty-schedule',
  standalone: false,
  templateUrl: './faculty-schedule.component.html',
  styleUrl: './faculty-schedule.component.css'
})
export class FacultyScheduleComponent implements OnInit {
  faculty: Faculty | null = null;
  scheduleRows: Array<FacultySchedule & { subjectName: string; departmentName: string }> = [];
  statusMessage = '';

  constructor(
    private readonly api: ErpApiService,
    private readonly facultySession: FacultySessionService
  ) {}

  ngOnInit(): void {
    const facultyId = this.facultySession.getFacultyId();
    if (!facultyId) {
      this.statusMessage = 'Faculty session not found.';
      return;
    }

    forkJoin({
      faculty: this.api.getFacultyById(facultyId),
      schedules: this.api.getFacultySchedules(),
      subjects: this.api.getSubjects()
    }).subscribe({
      next: ({ faculty, schedules, subjects }) => {
        this.faculty = faculty;
        const facultyDepartment = this.normalize(faculty.department);
        this.scheduleRows = schedules
          .filter(schedule => {
            const subject = subjects.find(entry => entry.subjectId === schedule.subjectId);
            const scheduleDepartment = this.normalize(schedule.departmentName);
            const subjectDepartment = this.normalize(subject?.departmentName);
            const assignedFacultyMatches = schedule.facultyId === faculty.facultyId;
            const subjectFacultyMatches = subject?.facultyId === faculty.facultyId;
            const departmentMatches = scheduleDepartment === facultyDepartment || subjectDepartment === facultyDepartment;
            return assignedFacultyMatches || subjectFacultyMatches || departmentMatches;
          })
          .map(schedule => {
            const subject = subjects.find(entry => entry.subjectId === schedule.subjectId);
            return {
              ...schedule,
              subjectName: subject?.name ?? `Subject #${schedule.subjectId}`,
              departmentName: subject?.departmentName ?? 'Unassigned'
            };
          });

        if (this.scheduleRows.length === 0) {
          this.statusMessage = 'No teaching slots are assigned to your profile or department yet.';
        }
      },
      error: () => {
        this.statusMessage = 'Unable to load faculty schedule data.';
      }
    });
  }

  private normalize(value: string | null | undefined): string {
    return (value ?? '').trim().toLowerCase();
  }
}
