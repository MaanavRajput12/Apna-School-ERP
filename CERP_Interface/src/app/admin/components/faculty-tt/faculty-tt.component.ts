import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Faculty, FacultySchedule, FacultySchedulePayload, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

@Component({
  selector: 'app-faculty-tt',
  standalone: false,
  templateUrl: './faculty-tt.component.html',
  styleUrl: './faculty-tt.component.css'
})
export class FacultyTTComponent implements OnInit {
  facultyTimetable: Array<FacultySchedule & {
    facultyName: string;
    subjectName: string;
    department: string;
    course: string;
    isEditing: boolean;
  }> = [];
  facultyList: Faculty[] = [];
  subjects: Subject[] = [];
  statusMessage = '';

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    forkJoin({
      facultySchedules: this.api.getFacultySchedules(),
      facultyList: this.api.getFaculty(),
      subjects: this.api.getSubjects()
    }).subscribe({
      next: ({ facultySchedules, facultyList, subjects }) => {
        this.facultyList = facultyList;
        this.subjects = subjects;
        this.facultyTimetable = facultySchedules.map(schedule => {
          const faculty = facultyList.find(entry => entry.facultyId === schedule.facultyId);
          const subject = subjects.find(entry => entry.subjectId === schedule.subjectId);

          return {
            ...schedule,
            facultyName: faculty?.facultyName ?? `Faculty #${schedule.facultyId}`,
            subjectName: subject?.name ?? `Subject #${schedule.subjectId}`,
            department: faculty?.department ?? 'Unassigned',
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
    const facultyId = this.facultyList.find(faculty => faculty.facultyName === row.facultyName)?.facultyId;
    const subjectId = this.subjects.find(subject => subject.name === row.subjectName)?.subjectId;

    if (!facultyId || !subjectId) {
      this.statusMessage = 'Faculty name and subject must match existing records before saving.';
      return;
    }

    const payload: FacultySchedulePayload = {
      faculty: { facultyId },
      subject: { subjectId },
      scheduleTime: row.scheduleTime,
      classroom: row.classroom
    };

    this.api.updateFacultySchedule(row.facultyScheduleId, payload).subscribe({
      next: updatedSchedule => {
        const faculty = this.facultyList.find(entry => entry.facultyId === updatedSchedule.facultyId);
        const subject = this.subjects.find(entry => entry.subjectId === updatedSchedule.subjectId);

        this.facultyTimetable[index] = {
          ...updatedSchedule,
          facultyName: faculty?.facultyName ?? row.facultyName,
          subjectName: subject?.name ?? row.subjectName,
          department: faculty?.department ?? row.department,
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
}
