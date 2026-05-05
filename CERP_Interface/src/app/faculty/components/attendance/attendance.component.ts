import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { AttendanceMarkRequest, AttendanceStatus, Department, DepartmentAttendanceStudent, Faculty, Subject } from '../../../core/models/erp.models';
import { FacultySessionService } from '../../services/faculty-session.service';
import { FacultyAttendanceApiService } from '../../services/faculty-attendance-api.service';

@Component({
  selector: 'app-attendance',
  standalone: false,
  templateUrl: './attendance.component.html',
  styleUrl: './attendance.component.css'
})
export class AttendanceComponent implements OnInit {
  faculty: Faculty | null = null;
  departments: Department[] = [];
  subjects: Subject[] = [];
  availableSubjects: Subject[] = [];
  students: DepartmentAttendanceStudent[] = [];
  attendanceMap: Record<number, AttendanceStatus> = {};
  errorMessage = '';
  successMessage = '';
  isLoadingDepartments = false;
  isLoadingStudents = false;
  isSubmitting = false;
  readonly attendanceForm;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly attendanceApi: FacultyAttendanceApiService,
    private readonly facultySession: FacultySessionService
  ) {
    this.attendanceForm = this.formBuilder.nonNullable.group({
      department: ['', Validators.required],
      subjectId: [0, [Validators.required, Validators.min(1)]],
      date: [new Date().toISOString().slice(0, 10), Validators.required]
    });
  }

  ngOnInit(): void {
    const facultyId = this.facultySession.getFacultyId();
    this.isLoadingDepartments = true;
    this.clearMessages();

    const request$ = forkJoin({
      departments: this.attendanceApi.getDepartments(),
      faculty: facultyId ? this.attendanceApi.getFacultyById(facultyId) : of<Faculty | null>(null),
      subjects: this.attendanceApi.getSubjects()
    });

    request$.subscribe({
      next: ({ departments, faculty, subjects }) => {
        this.departments = departments;
        this.faculty = faculty;
        this.subjects = subjects.filter(subject => subject.active !== false);
        this.isLoadingDepartments = false;

        if (faculty?.department) {
          this.attendanceForm.patchValue({ department: faculty.department });
          this.onDepartmentChange(faculty.department);
        }
      },
      error: () => {
        this.isLoadingDepartments = false;
        this.errorMessage = 'Unable to load attendance setup data.';
      }
    });
  }

  onDepartmentChange(department: string): void {
    this.clearMessages();
    this.students = [];
    this.attendanceMap = {};
    this.availableSubjects = this.subjects.filter(subject => {
      const departmentMatches = subject.departmentName?.toLowerCase() === department.toLowerCase();
      const facultyMatches = this.faculty?.facultyId ? subject.facultyId === this.faculty.facultyId : true;
      return departmentMatches && facultyMatches;
    });

    const currentSubjectId = this.attendanceForm.controls.subjectId.value;
    const subjectStillValid = this.availableSubjects.some(subject => subject.subjectId === currentSubjectId);
    if (!subjectStillValid) {
      this.attendanceForm.patchValue({ subjectId: 0 });
    }

    if (!department) {
      this.isLoadingStudents = false;
      return;
    }

    this.isLoadingStudents = true;
    this.attendanceApi.getStudentsByDepartment(department).subscribe({
      next: students => {
        this.students = students;
        this.isLoadingStudents = false;
      },
      error: () => {
        this.isLoadingStudents = false;
        this.errorMessage = 'Unable to load students for the selected department.';
      }
    });
  }

  updateAttendance(studentId: number, status: AttendanceStatus): void {
    this.attendanceMap = {
      ...this.attendanceMap,
      [studentId]: status
    };
  }

  submitAttendance(): void {
    this.clearMessages();

    if (this.attendanceForm.invalid) {
      this.errorMessage = 'Select a department, subject, and date before submitting attendance.';
      this.attendanceForm.markAllAsTouched();
      return;
    }

    if (this.students.length === 0) {
      this.errorMessage = 'There are no students available for the selected department.';
      return;
    }

    const unmarkedStudents = this.students.filter(student => !this.attendanceMap[student.studentId]);
    if (unmarkedStudents.length > 0) {
      this.errorMessage = 'Mark every listed student as present or absent before submitting.';
      return;
    }

    const selectedDate = this.attendanceForm.controls.date.value;
    const selectedSubjectId = this.attendanceForm.controls.subjectId.value;
    const payload: AttendanceMarkRequest[] = this.students.map(student => ({
      studentId: student.studentId,
      date: selectedDate,
      subjectId: selectedSubjectId,
      status: this.attendanceMap[student.studentId]
    }));

    this.isSubmitting = true;
    this.attendanceApi.markAttendance(payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = 'Attendance submitted successfully.';
        const department = this.attendanceForm.controls.department.value;
        this.onDepartmentChange(department);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error?.error?.message ?? 'Unable to submit attendance right now.';
      }
    });
  }

  trackByStudentId(_: number, student: DepartmentAttendanceStudent): number {
    return student.studentId;
  }

  get selectedDepartment(): string {
    return this.attendanceForm.controls.department.value;
  }

  get selectedSubjectId(): number {
    return this.attendanceForm.controls.subjectId.value;
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}
