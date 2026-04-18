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
  students: Student[] = [];
  examRows: Array<Exam & { department: string }> = [];
  selectedStudentId: number | null = null;
  statusMessage = '';

  constructor(
    private readonly api: ErpApiService,
    private readonly studentSession: StudentSessionService
  ) {}

  ngOnInit(): void {
    this.selectedStudentId = this.studentSession.getStudentId();
    this.loadData();
  }

  loadData(): void {
    forkJoin({
      students: this.api.getStudents(),
      exams: this.api.getExams(),
      subjects: this.api.getSubjects(),
      facultyList: this.api.getFaculty()
    }).subscribe({
      next: ({ students, exams, subjects, facultyList }) => {
        this.students = students;
        if (!this.selectedStudentId && students.length > 0) {
          this.selectedStudentId = students[0].studentId;
        }
        this.refreshRows(exams, subjects, facultyList);
      },
      error: () => {
        this.statusMessage = 'Unable to load exam schedule.';
      }
    });
  }

  onStudentChange(studentId: number | null): void {
    this.selectedStudentId = studentId;
    if (studentId) {
      this.studentSession.setStudentId(studentId);
    }
    this.loadData();
  }

  get selectedStudent(): Student | undefined {
    return this.students.find(student => student.studentId === this.selectedStudentId);
  }

  private refreshRows(exams: Exam[], subjects: Subject[], facultyList: Faculty[]): void {
    const department = this.selectedStudent?.departmentName;
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
