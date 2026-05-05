import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { AttendancePercentageResponse, Student, SubjectAttendancePercentage } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { StudentSessionService } from '../../services/student-session.service';

@Component({
  selector: 'app-attendance',
  standalone: false,
  templateUrl: './attendance.component.html',
  styleUrl: './attendance.component.css'
})
export class AttendanceComponent implements OnInit {
  student: Student | null = null;
  attendancePercentage: number | null = null;
  subjectWiseAttendance: SubjectAttendancePercentage[] = [];
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
      attendancePercentage: this.api.getAttendancePercentage(studentId),
      subjectWiseAttendance: this.api.getStudentSubjectAttendancePercentages(studentId)
    }).subscribe({
      next: ({ student, attendancePercentage, subjectWiseAttendance }) => {
        this.student = student;
        this.applyAttendancePercentage(attendancePercentage);
        this.subjectWiseAttendance = subjectWiseAttendance;
      },
      error: () => {
        this.statusMessage = 'Unable to load attendance data.';
      }
    });
  }

  private applyAttendancePercentage(response: AttendancePercentageResponse): void {
    this.attendancePercentage = response.percentage;
  }

  trackBySubjectId(_: number, row: SubjectAttendancePercentage): number {
    return row.subjectId;
  }
}
