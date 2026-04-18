import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Faculty, FacultySchedule, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { FacultySessionService } from '../../services/faculty-session.service';

@Component({
  selector: 'app-faculty-schedule',
  standalone: false,
  templateUrl: './faculty-schedule.component.html',
  styleUrl: './faculty-schedule.component.css'
})
export class FacultyScheduleComponent implements OnInit {
  facultyList: Faculty[] = [];
  scheduleRows: Array<FacultySchedule & { subjectName: string; courseName: string }> = [];
  selectedFacultyId: number | null = null;

  constructor(
    private readonly api: ErpApiService,
    private readonly facultySession: FacultySessionService
  ) {}

  ngOnInit(): void {
    this.selectedFacultyId = this.facultySession.getFacultyId();
    forkJoin({
      facultyList: this.api.getFaculty(),
      schedules: this.api.getFacultySchedules(),
      subjects: this.api.getSubjects()
    }).subscribe(({ facultyList, schedules, subjects }) => {
      this.facultyList = facultyList;
      if (!this.selectedFacultyId && facultyList.length > 0) {
        this.selectedFacultyId = facultyList[0].facultyId;
      }
      this.refreshRows(schedules, subjects);
    });
  }

  changeFaculty(facultyId: number | null): void {
    this.selectedFacultyId = facultyId;
    if (facultyId) {
      this.facultySession.setFacultyId(facultyId);
    }
    this.ngOnInit();
  }

  get selectedFaculty(): Faculty | undefined {
    return this.facultyList.find(faculty => faculty.facultyId === this.selectedFacultyId);
  }

  private refreshRows(schedules: FacultySchedule[], subjects: Subject[]): void {
    this.scheduleRows = schedules
      .filter(schedule => schedule.facultyId === this.selectedFacultyId)
      .map(schedule => {
        const subject = subjects.find(entry => entry.subjectId === schedule.subjectId);
        return {
          ...schedule,
          subjectName: subject?.name ?? `Subject #${schedule.subjectId}`,
          courseName: subject?.courseName ?? 'Unassigned'
        };
      });
  }
}
