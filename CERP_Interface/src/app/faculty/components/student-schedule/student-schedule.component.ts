import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Faculty, Timetable } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { FacultySessionService } from '../../services/faculty-session.service';

@Component({
  selector: 'app-student-schedule',
  standalone: false,
  templateUrl: './student-schedule.component.html',
  styleUrl: './student-schedule.component.css'
})
export class StudentScheduleComponent implements OnInit {
  facultyList: Faculty[] = [];
  scheduleRows: Timetable[] = [];
  selectedFacultyId: number | null = null;

  constructor(
    private readonly api: ErpApiService,
    private readonly facultySession: FacultySessionService
  ) {}

  ngOnInit(): void {
    this.selectedFacultyId = this.facultySession.getFacultyId();
    forkJoin({
      facultyList: this.api.getFaculty(),
      timetables: this.api.getTimetables()
    }).subscribe(({ facultyList, timetables }) => {
      this.facultyList = facultyList;
      if (!this.selectedFacultyId && facultyList.length > 0) {
        this.selectedFacultyId = facultyList[0].facultyId;
      }
      this.refreshRows(timetables);
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

  private refreshRows(timetables: Timetable[]): void {
    const department = this.selectedFaculty?.department;
    this.scheduleRows = timetables.filter(row => !department || row.departmentName === department);
  }
}
