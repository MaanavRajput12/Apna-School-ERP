import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Student, Timetable } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { StudentSessionService } from '../../services/student-session.service';

@Component({
  selector: 'app-timetable',
  standalone: false,
  templateUrl: './timetable.component.html',
  styleUrl: './timetable.component.css'
})
export class TimetableComponent implements OnInit {
  student: Student | null = null;
  timetableRows: Timetable[] = [];
  statusMessage = '';

  constructor(
    private readonly api: ErpApiService,
    private readonly studentSession: StudentSessionService
  ) {}

  ngOnInit(): void {
    const studentId = this.studentSession.getStudentId();
    if (!studentId) {
      this.statusMessage = 'Student session not found.';
      return;
    }

    forkJoin({
      student: this.api.getStudent(studentId),
      timetables: this.api.getTimetables()
    }).subscribe({
      next: ({ student, timetables }) => {
        this.student = student;
        const department = this.normalize(student.departmentName);
        const semester = this.normalizeSemester(student.semester);
        this.timetableRows = timetables.filter(row => {
          const rowDepartment = this.normalize(row.departmentName);
          const rowSemester = this.normalizeSemester(row.semester);
          const departmentMatches = !department || rowDepartment === department;
          const semesterMatches = !semester || !rowSemester || rowSemester === semester;
          return departmentMatches && semesterMatches;
        });
      },
      error: () => {
        this.statusMessage = 'Unable to load timetable data.';
      }
    });
  }

  private normalize(value: string | null | undefined): string {
    return (value ?? '').trim().toLowerCase();
  }

  private normalizeSemester(value: string | number | null | undefined): string {
    const normalized = String(value ?? '').trim().toLowerCase();
    const digits = normalized.replace(/[^0-9]/g, '');
    return digits || normalized;
  }
}
