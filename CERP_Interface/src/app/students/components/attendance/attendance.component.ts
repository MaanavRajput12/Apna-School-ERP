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
  students: Student[] = [];
  attendanceRows: AttendanceSummary[] = [];
  selectedStudentId: number | null = null;
  statusMessage = '';

  constructor(
    private readonly api: ErpApiService,
    private readonly studentSession: StudentSessionService
  ) {}

  ngOnInit(): void {
    this.selectedStudentId = this.studentSession.getStudentId();
    this.loadData();
  }

  loadData(): void {
    forkJoin({
      students: this.api.getStudents(),
      attendance: this.api.getAttendance(),
      subjects: this.api.getSubjects()
    }).subscribe({
      next: ({ students, attendance, subjects }) => {
        this.students = students;
        if (!this.selectedStudentId && students.length > 0) {
          this.selectedStudentId = students[0].studentId;
        }
        this.refreshSummary(attendance, subjects);
      },
      error: () => {
        this.statusMessage = 'Unable to load attendance data.';
      }
    });
  }

  onStudentChange(studentId: number | null): void {
    this.selectedStudentId = studentId;
    if (studentId) {
      this.studentSession.setStudentId(studentId);
    }
    this.loadData();
  }

  get selectedStudent(): Student | undefined {
    return this.students.find(student => student.studentId === this.selectedStudentId);
  }

  private refreshSummary(attendance: Attendance[], subjects: Subject[]): void {
    const studentAttendance = attendance.filter(row => row.studentId === this.selectedStudentId);
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
