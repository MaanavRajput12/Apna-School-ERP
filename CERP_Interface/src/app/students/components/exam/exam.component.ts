import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Exam, Faculty, Student, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { StudentSessionService } from '../../services/student-session.service';

@Component({
  selector: 'app-exam',
  standalone: false,
  templateUrl: './exam.component.html',
  styleUrl: './exam.component.css'
})
export class ExamComponent implements OnInit {
  student: Student | null = null;
  examRows: Array<Exam & { department: string }> = [];
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
      exams: this.api.getExams(),
      subjects: this.api.getSubjects(),
      facultyList: this.api.getFaculty()
    }).subscribe({
      next: ({ student, exams, subjects, facultyList }) => {
        this.student = student;
        this.refreshRows(exams, subjects, facultyList, student.departmentName);
      },
      error: () => {
        this.statusMessage = 'Unable to load exam schedule.';
      }
    });
  }

  private refreshRows(exams: Exam[], subjects: Subject[], facultyList: Faculty[], department: string | null): void {
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
