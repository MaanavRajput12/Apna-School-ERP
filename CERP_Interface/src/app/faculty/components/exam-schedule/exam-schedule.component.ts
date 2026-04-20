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
  faculty: Faculty | null = null;
  examRows: Array<Exam & { department: string }> = [];

  constructor(
    private readonly api: ErpApiService,
    private readonly facultySession: FacultySessionService
  ) {}

  ngOnInit(): void {
    const facultyId = this.facultySession.getFacultyId();
    if (!facultyId) {
      return;
    }

    forkJoin({
      faculty: this.api.getFacultyById(facultyId),
      exams: this.api.getExams(),
      subjects: this.api.getSubjects(),
      facultyList: this.api.getFaculty()
    }).subscribe(({ faculty, exams, subjects, facultyList }) => {
      this.faculty = faculty;
      this.examRows = exams
        .map(exam => {
          const subject = subjects.find(item => item.name === exam.subjectName);
          const examFaculty = facultyList.find(item => item.facultyName === subject?.facultyName);
          return {
            ...exam,
            department: examFaculty?.department ?? 'Unassigned'
          };
        })
        .filter(exam => !faculty.department || exam.department === faculty.department);
    });
  }
}
