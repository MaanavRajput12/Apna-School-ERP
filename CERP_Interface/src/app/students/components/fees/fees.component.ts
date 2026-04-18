import { Component, OnInit } from '@angular/core';
import { Fee, Student } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { StudentSessionService } from '../../services/student-session.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-fees',
  standalone: false,
  templateUrl: './fees.component.html',
  styleUrl: './fees.component.css'
})
export class FeesComponent implements OnInit {
  students: Student[] = [];
  feeRows: Fee[] = [];
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
      fees: this.api.getFees()
    }).subscribe({
      next: ({ students, fees }) => {
        this.students = students;
        this.feeRows = fees;
        if (!this.selectedStudentId && students.length > 0) {
          this.selectedStudentId = students[0].studentId;
        }
      },
      error: () => {
        this.statusMessage = 'Unable to load fee data.';
      }
    });
  }

  onStudentChange(studentId: number | null): void {
    this.selectedStudentId = studentId;
    if (studentId) {
      this.studentSession.setStudentId(studentId);
    }
  }

  get selectedStudent(): Student | undefined {
    return this.students.find(student => student.studentId === this.selectedStudentId);
  }

  get highlightedFee(): Fee | undefined {
    if (!this.feeRows.length) {
      return undefined;
    }
    const status = this.selectedStudent?.feesStatus;
    return this.feeRows.find(fee => fee.feesStatus === status) ?? this.feeRows[0];
  }

  get pendingAmount(): number {
    const fee = this.highlightedFee;
    if (!fee) {
      return 0;
    }
    return fee.feesStatus?.toLowerCase().includes('paid') ? 0 : fee.amount;
  }
}
