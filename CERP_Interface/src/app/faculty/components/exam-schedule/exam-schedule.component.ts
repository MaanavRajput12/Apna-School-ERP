import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Exam, Faculty, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { FacultySessionService } from '../../services/faculty-session.service';

@Component({
  selector: 'app-exam-schedule',
  standalone: false,
  templateUrl: './exam-schedule.component.html',
  styleUrl: './exam-schedule.component.css'
})
export class ExamScheduleComponent implements OnInit {
  facultyList: Faculty[] = [];
  examRows: Array<Exam & { department: string }> = [];
  selectedFacultyId: number | null = null;

  constructor(
    private readonly api: ErpApiService,
    private readonly facultySession: FacultySessionService
  ) {}

  ngOnInit(): void {
    this.selectedFacultyId = this.facultySession.getFacultyId();
    forkJoin({
      facultyList: this.api.getFaculty(),
      exams: this.api.getExams(),
      subjects: this.api.getSubjects()
    }).subscribe(({ facultyList, exams, subjects }) => {
      this.facultyList = facultyList;
      if (!this.selectedFacultyId && facultyList.length > 0) {
        this.selectedFacultyId = facultyList[0].facultyId;
      }
      this.refreshRows(exams, subjects, facultyList);
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

  private refreshRows(exams: Exam[], subjects: Subject[], facultyList: Faculty[]): void {
    const department = this.selectedFaculty?.department;
    this.examRows = exams
      .map(exam => {
        const subject = subjects.find(item => item.name === exam.subjectName);
        const faculty = facultyList.find(item => item.facultyName === subject?.facultyName);
        return {
          ...exam,
          department: faculty?.department ?? 'Unassigned'
        };
      })
      .filter(exam => !department || exam.department === department);
  }
}
