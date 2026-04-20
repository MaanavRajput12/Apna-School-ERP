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
  scheduleRows: Array<FacultySchedule & { subjectName: string; courseName: string }> = [];

  constructor(
    private readonly api: ErpApiService,
    private readonly facultySession: FacultySessionService
  ) {}

  ngOnInit(): void {
    const facultyId = this.facultySession.getFacultyId();
    if (!facultyId) {
      return;
    }

    forkJoin({
      faculty: this.api.getFacultyById(facultyId),
      schedules: this.api.getFacultySchedules(),
      subjects: this.api.getSubjects()
    }).subscribe(({ faculty, schedules, subjects }) => {
      this.faculty = faculty;
      this.scheduleRows = schedules
        .filter(schedule => schedule.departmentName === faculty.department)
        .map(schedule => {
          const subject = subjects.find(entry => entry.subjectId === schedule.subjectId);
          return {
            ...schedule,
            subjectName: subject?.name ?? `Subject #${schedule.subjectId}`,
            courseName: subject?.courseName ?? 'Unassigned'
          };
        });
    });
  }
}
