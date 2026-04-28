import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Fee, Student } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { StudentSessionService } from '../../services/student-session.service';

@Component({
  selector: 'app-fees',
  standalone: false,
  templateUrl: './fees.component.html',
  styleUrl: './fees.component.css'
})
export class FeesComponent implements OnInit {
  student: Student | null = null;
  fee: Fee | null = null;
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
      fees: this.api.getFees()
    }).subscribe({
      next: ({ student, fees }) => {
        this.student = student;
        this.fee = fees.find(item => item.studentId === student.studentId) ?? null;
      },
      error: () => {
        this.statusMessage = 'Unable to load fee data.';
      }
    });
  }

  get pendingAmount(): number {
    if (!this.fee) {
      return 0;
    }
    return this.fee.feesStatus?.toLowerCase().includes('paid') ? 0 : this.fee.amount;
  }

  get effectiveFeeStatus(): string {
    return this.fee?.feesStatus || this.student?.feesStatus || 'Status unavailable';
  }
}
