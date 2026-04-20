import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Attendance, Student, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { StudentSessionService } from '../../services/student-session.service';

interface AttendanceSummary {
  subjectId: number;
  subjectName: string;
  attended: number;
  total: number;
  percentage: number;
  status: string;
}

@Component({
  selector: 'app-attendance',
  standalone: false,
  templateUrl: './attendance.component.html',
  styleUrl: './attendance.component.css'
})
export class AttendanceComponent implements OnInit {
  student: Student | null = null;
  attendanceRows: AttendanceSummary[] = [];
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
      attendance: this.api.getAttendance(),
      subjects: this.api.getSubjects()
    }).subscribe({
      next: ({ student, attendance, subjects }) => {
        this.student = student;
        this.refreshSummary(attendance, subjects, student.studentId);
      },
      error: () => {
        this.statusMessage = 'Unable to load attendance data.';
      }
    });
  }

  private refreshSummary(attendance: Attendance[], subjects: Subject[], studentId: number): void {
    const studentAttendance = attendance.filter(row => row.studentId === studentId);
    const summaryMap = new Map<number, AttendanceSummary>();

    studentAttendance.forEach(row => {
      if (!row.subjectId) {
        return;
      }
      const subject = subjects.find(item => item.subjectId === row.subjectId);
      const current = summaryMap.get(row.subjectId) ?? {
        subjectId: row.subjectId,
        subjectName: subject?.name ?? `Subject #${row.subjectId}`,
        attended: 0,
        total: 0,
        percentage: 0,
        status: 'Monitor'
      };

      current.total += 1;
      current.attended += row.present ? 1 : 0;
      current.percentage = current.total === 0 ? 0 : Number(((current.attended / current.total) * 100).toFixed(1));
      current.status = current.percentage >= 85 ? 'On Track' : current.percentage >= 75 ? 'Comfortable' : 'Monitor';
      summaryMap.set(row.subjectId, current);
    });

    this.attendanceRows = Array.from(summaryMap.values());
  }
}
