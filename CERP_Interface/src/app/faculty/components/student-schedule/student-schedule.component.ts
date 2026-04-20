import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Faculty, Timetable } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { FacultySessionService } from '../../services/faculty-session.service';

@Component({
  selector: 'app-student-schedule',
  standalone: false,
  templateUrl: './student-schedule.component.html',
  styleUrl: './student-schedule.component.css'
})
export class StudentScheduleComponent implements OnInit {
  faculty: Faculty | null = null;
  scheduleRows: Timetable[] = [];

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
      timetables: this.api.getTimetables()
    }).subscribe(({ faculty, timetables }) => {
      this.faculty = faculty;
      this.scheduleRows = timetables.filter(row => !faculty.department || row.departmentName === faculty.department);
    });
  }
}
