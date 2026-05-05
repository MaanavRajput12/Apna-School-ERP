import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Faculty, Student, Timetable } from '../../../core/models/erp.models';
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
      timetables: this.api.getTimetables(),
      facultyList: this.api.getFaculty()
    }).subscribe({
      next: ({ student, timetables, facultyList }) => {
        this.student = student;
        const department = this.normalize(student.departmentName);
        const semester = this.normalizeSemester(student.semester);
        this.timetableRows = timetables.filter(row => {
          const rowDepartment = this.normalize(row.departmentName);
          const rowSemester = this.normalizeSemester(row.semester);
          const facultyDepartment = this.getFacultyDepartment(row.facultyName, facultyList);
          const departmentMatches = !department || rowDepartment === department;
          const fallbackDepartmentMatches = !department || facultyDepartment === department;
          const semesterMatches = !semester || !rowSemester || rowSemester === semester;
          return (departmentMatches || fallbackDepartmentMatches) && semesterMatches;
        });

        if (this.timetableRows.length === 0) {
          this.statusMessage = 'No timetable rows matched your department and semester yet.';
        }
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

  private getFacultyDepartment(facultyName: string | null | undefined, facultyList: Faculty[]): string {
    const faculty = facultyList.find(row => this.normalize(row.facultyName) === this.normalize(facultyName));
    return this.normalize(faculty?.department);
  }
}
