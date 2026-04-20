import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Department, FacultySchedule, FacultySchedulePayload, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

type EditableFacultySchedule = FacultySchedule & {
  subjectName: string;
  course: string;
  isEditing: boolean;
};

@Component({
  selector: 'app-faculty-tt',
  standalone: false,
  templateUrl: './faculty-tt.component.html',
  styleUrl: './faculty-tt.component.css'
})
export class FacultyTTComponent implements OnInit {
  facultyTimetable: EditableFacultySchedule[] = [];
  departments: Department[] = [];
  subjects: Subject[] = [];
  statusMessage = '';
  newSchedule: EditableFacultySchedule = {
    facultyScheduleId: 0,
    facultyId: null,
    departmentId: null,
    departmentName: null,
    subjectId: null,
    subjectName: '',
    course: 'Unassigned',
    scheduleTime: '',
    classroom: '',
    isEditing: false
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    forkJoin({
      facultySchedules: this.api.getFacultySchedules(),
      departments: this.api.getDepartments(),
      subjects: this.api.getSubjects()
    }).subscribe({
      next: ({ facultySchedules, departments, subjects }) => {
        this.departments = departments;
        this.subjects = subjects;
        this.facultyTimetable = facultySchedules.map(schedule => {
          const subject = subjects.find(entry => entry.subjectId === schedule.subjectId);

          return {
            ...schedule,
            subjectName: subject?.name ?? `Subject #${schedule.subjectId}`,
            course: subject?.courseName ?? 'Unassigned',
            isEditing: false
          };
        });
      },
      error: () => {
        this.statusMessage = 'Unable to load faculty schedule records.';
      }
    });
  }

  editRow(index: number): void {
    this.facultyTimetable[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.facultyTimetable[index];
    const subjectId = this.subjects.find(subject => subject.name === row.subjectName)?.subjectId;

    if (!row.departmentId || !subjectId) {
      this.statusMessage = 'Department and subject must match existing records before saving.';
      return;
    }

    const payload: FacultySchedulePayload = {
      department: { departmentId: row.departmentId },
      subject: { subjectId },
      scheduleTime: row.scheduleTime,
      classroom: row.classroom,
      faculty: null
    };

    this.api.updateFacultySchedule(row.facultyScheduleId, payload).subscribe({
      next: updatedSchedule => {
        const subject = this.subjects.find(entry => entry.subjectId === updatedSchedule.subjectId);

        this.facultyTimetable[index] = {
          ...updatedSchedule,
          subjectName: subject?.name ?? row.subjectName,
          course: subject?.courseName ?? row.course,
          isEditing: false
        };
        this.statusMessage = 'Faculty schedule updated successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not update the selected faculty schedule.';
      }
    });
  }

  addSchedule(): void {
    const subjectId = this.subjects.find(subject => subject.name === this.newSchedule.subjectName)?.subjectId;

    if (!this.newSchedule.departmentId || !subjectId) {
      this.statusMessage = 'Select a department and subject before creating the faculty timetable.';
      return;
    }

    const payload: FacultySchedulePayload = {
      department: { departmentId: this.newSchedule.departmentId },
      subject: { subjectId },
      scheduleTime: this.newSchedule.scheduleTime,
      classroom: this.newSchedule.classroom,
      faculty: null
    };

    this.api.createFacultySchedule(payload).subscribe({
      next: createdSchedule => {
        const subject = this.subjects.find(entry => entry.subjectId === createdSchedule.subjectId);
        this.facultyTimetable = [{
          ...createdSchedule,
          subjectName: subject?.name ?? this.newSchedule.subjectName,
          course: subject?.courseName ?? 'Unassigned',
          isEditing: false
        }, ...this.facultyTimetable];
        this.newSchedule = {
          facultyScheduleId: 0,
          facultyId: null,
          departmentId: null,
          departmentName: null,
          subjectId: null,
          subjectName: '',
          course: 'Unassigned',
          scheduleTime: '',
          classroom: '',
          isEditing: false
        };
        this.statusMessage = 'Faculty timetable created successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not create the faculty timetable row.';
      }
    });
  }

  onSubjectChange(subjectName: string): void {
    const subject = this.subjects.find(item => item.name === subjectName);
    this.newSchedule.subjectName = subjectName;
    this.newSchedule.course = subject?.courseName ?? 'Unassigned';
  }

  onRowSubjectChange(row: EditableFacultySchedule, subjectName: string): void {
    const subject = this.subjects.find(item => item.name === subjectName);
    row.subjectName = subjectName;
    row.course = subject?.courseName ?? 'Unassigned';
  }
}
