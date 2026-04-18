import { Component, OnInit } from '@angular/core';
import { Student, Timetable } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { StudentSessionService } from '../../services/student-session.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-timetable',
  standalone: false,
  templateUrl: './timetable.component.html',
  styleUrl: './timetable.component.css'
})
export class TimetableComponent implements OnInit {
  students: Student[] = [];
  timetableRows: Timetable[] = [];
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
      timetables: this.api.getTimetables()
    }).subscribe({
      next: ({ students, timetables }) => {
        this.students = students;
        if (!this.selectedStudentId && students.length > 0) {
          this.selectedStudentId = students[0].studentId;
        }
        this.refreshRows(timetables);
      },
      error: () => {
        this.statusMessage = 'Unable to load timetable data.';
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

  private refreshRows(timetables: Timetable[]): void {
    const department = this.selectedStudent?.departmentName;
    const semester = this.selectedStudent?.semester;
    this.timetableRows = timetables.filter(row => {
      const departmentMatches = !department || row.departmentName === department;
      const semesterMatches = !semester || String(row.semester) === String(semester);
      return departmentMatches && semesterMatches;
    });
  }
}
