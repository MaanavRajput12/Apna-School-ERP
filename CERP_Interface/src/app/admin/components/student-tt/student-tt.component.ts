import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Department, Faculty, Timetable, TimetablePayload } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

@Component({
  selector: 'app-student-tt',
  standalone: false,
  templateUrl: './student-tt.component.html',
  styleUrl: './student-tt.component.css'
})
export class StudentTTComponent implements OnInit {
  timetable: Array<Timetable & { isEditing: boolean }> = [];
  departments: Department[] = [];
  facultyList: Faculty[] = [];
  statusMessage = '';
  newTimetable: Timetable & { departmentId: number | null; facultyId: number | null } = {
    timeTableId: 0,
    semester: 1,
    day: '',
    timeSlot: '',
    departmentName: null,
    facultyName: null,
    departmentId: null,
    facultyId: null
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadTimetables();
  }

  loadTimetables(): void {
    forkJoin({
      timetables: this.api.getTimetables(),
      departments: this.api.getDepartments(),
      facultyList: this.api.getFaculty()
    }).subscribe({
      next: ({ timetables, departments, facultyList }) => {
        this.timetable = timetables.map(row => ({ ...row, isEditing: false }));
        this.departments = departments;
        this.facultyList = facultyList;
      },
      error: () => {
        this.statusMessage = 'Unable to load student timetable records.';
      }
    });
  }

  editRow(index: number): void {
    this.timetable[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.timetable[index];
    const departmentId = this.departments.find(department => department.departmentName === row.departmentName)?.departmentId;
    const facultyId = this.facultyList.find(faculty => faculty.facultyName === row.facultyName)?.facultyId;

    if (!departmentId || !facultyId) {
      this.statusMessage = 'Department and faculty must match existing records before saving.';
      return;
    }

    const payload: TimetablePayload = {
      semester: Number(row.semester),
      day: row.day,
      timeSlot: row.timeSlot,
      department: { departmentId },
      faculty: { facultyId }
    };

    this.api.updateTimetable(row.timeTableId, payload).subscribe({
      next: updatedRow => {
        this.timetable[index] = { ...updatedRow, isEditing: false };
        this.statusMessage = 'Student schedule updated successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not update the selected student schedule.';
      }
    });
  }

  addTimetable(): void {
    if (!this.newTimetable.departmentId || !this.newTimetable.facultyId) {
      this.statusMessage = 'Select a department and faculty before creating the timetable row.';
      return;
    }

    const payload: TimetablePayload = {
      semester: Number(this.newTimetable.semester),
      day: this.newTimetable.day,
      timeSlot: this.newTimetable.timeSlot,
      department: { departmentId: this.newTimetable.departmentId },
      faculty: { facultyId: this.newTimetable.facultyId }
    };

    this.api.createTimetable(payload).subscribe({
      next: createdRow => {
        this.timetable = [{ ...createdRow, isEditing: false }, ...this.timetable];
        this.newTimetable = {
          timeTableId: 0,
          semester: 1,
          day: '',
          timeSlot: '',
          departmentName: null,
          facultyName: null,
          departmentId: null,
          facultyId: null
        };
        this.statusMessage = 'Student timetable created successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not create the student timetable row.';
      }
    });
  }

  getFacultyForDepartment(departmentName: string | null): Faculty[] {
    if (!departmentName) {
      return this.facultyList;
    }
    return this.facultyList.filter(faculty => faculty.department === departmentName);
  }

  onNewDepartmentChange(departmentId: number | null): void {
    this.newTimetable.departmentId = departmentId;
    const departmentName = this.departments.find(department => department.departmentId === departmentId)?.departmentName ?? null;
    this.newTimetable.departmentName = departmentName;
    this.newTimetable.facultyId = null;
  }

  onDepartmentChange(row: Timetable, departmentName: string | null): void {
    row.departmentName = departmentName;
    row.facultyName = null;
  }
}
