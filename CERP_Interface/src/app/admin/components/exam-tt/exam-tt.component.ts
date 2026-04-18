import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Exam, ExamPayload, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

@Component({
  selector: 'app-exam-tt',
  standalone: false,
  templateUrl: './exam-tt.component.html',
  styleUrl: './exam-tt.component.css'
})
export class ExamTTComponent implements OnInit {
  exams: Array<Exam & { isEditing: boolean }> = [];
  subjects: Subject[] = [];
  statusMessage = '';

  newExam: ExamPayload = {
    examType: '',
    examDate: '',
    examTime: '',
    totaMarks: 0,
    subjectName: '',
    subject: null
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadExams();
  }

  loadExams(): void {
    forkJoin({
      exams: this.api.getExams(),
      subjects: this.api.getSubjects()
    }).subscribe({
      next: ({ exams, subjects }) => {
        this.exams = exams.map(exam => ({ ...exam, isEditing: false }));
        this.subjects = subjects;
      },
      error: () => {
        this.statusMessage = 'Unable to load exam schedule data.';
      }
    });
  }

  addExam(): void {
    const payload = this.withSubjectReference(this.newExam);
    this.api.createExam(payload).subscribe({
      next: createdExam => {
        this.exams = [{ ...createdExam, isEditing: false }, ...this.exams];
        this.newExam = {
          examType: '',
          examDate: '',
          examTime: '',
          totaMarks: 0,
          subjectName: '',
          subject: null
        };
        this.statusMessage = 'Exam schedule added successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not add the exam schedule.';
      }
    });
  }

  editRow(index: number): void {
    this.exams[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.exams[index];
    const payload = this.withSubjectReference({
      examType: row.examType,
      examDate: row.examDate,
      examTime: row.examTime,
      totaMarks: row.totalMarks,
      subjectName: row.subjectName
    });

    this.api.updateExam(row.examId, payload).subscribe({
      next: updatedExam => {
        this.exams[index] = { ...updatedExam, isEditing: false };
        this.statusMessage = 'Exam schedule updated successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not update the exam schedule.';
      }
    });
  }

  deleteExam(examId: number): void {
    this.api.deleteExam(examId).subscribe({
      next: () => {
        this.exams = this.exams.filter(exam => exam.examId !== examId);
        this.statusMessage = 'Exam schedule removed successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not remove the exam schedule.';
      }
    });
  }

  private withSubjectReference(payload: ExamPayload): ExamPayload {
    const subjectId = this.subjects.find(subject => subject.name === payload.subjectName)?.subjectId;
    return {
      ...payload,
      subject: subjectId ? { subjectId } : null
    };
  }
}
