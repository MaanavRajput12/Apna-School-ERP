import { Component, OnInit } from '@angular/core';
import { Fee, Student } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

@Component({
  selector: 'app-studentfees',
  standalone: false,
  templateUrl: './studentfees.component.html',
  styleUrl: './studentfees.component.css'
})
export class StudentfeesComponent implements OnInit {
  students: Student[] = [];
  fees: Array<Fee & { isEditing: boolean }> = [];
  selectedStudentId: number | null = null;
  statusMessage = '';

  draftFee: Fee = {
    feesId: 0,
    amount: 0,
    feesStatus: '',
    dueDate: '',
    studentId: null,
    studentName: null
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadFees();
  }

  loadFees(): void {
    this.api.getStudents().subscribe({
      next: students => {
        this.students = students;
      }
    });

    this.api.getFees().subscribe({
      next: fees => {
        this.fees = fees.map(fee => ({ ...fee, isEditing: false }));
      },
      error: () => {
        this.statusMessage = 'Unable to load student fee records.';
      }
    });
  }

  editRow(index: number): void {
    this.selectedStudentId = this.fees[index].studentId;
    this.fees[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.fees[index];
    this.api.updateFee(row.feesId, {
      amount: Number(row.amount),
      feesStatus: row.feesStatus,
      dueDate: row.dueDate,
      studentId: row.studentId
    }).subscribe({
      next: updatedFee => {
        this.fees[index] = { ...updatedFee, isEditing: false };
        this.statusMessage = 'Fee record updated successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not update the fee record.';
      }
    });
  }

  createOrUpdateForSelectedStudent(): void {
    if (!this.selectedStudentId) {
      this.statusMessage = 'Select a student before editing fees.';
      return;
    }

    const existingFee = this.fees.find(fee => fee.studentId === this.selectedStudentId);
    const payload = {
      amount: Number(this.draftFee.amount),
      feesStatus: this.draftFee.feesStatus,
      dueDate: this.draftFee.dueDate,
      studentId: this.selectedStudentId
    };

    const request = existingFee
      ? this.api.updateFee(existingFee.feesId, payload)
      : this.api.createFee(payload);

    request.subscribe({
      next: fee => {
        this.statusMessage = existingFee ? 'Fee record updated successfully.' : 'Fee record created successfully.';
        this.draftFee = {
          feesId: 0,
          amount: 0,
          feesStatus: '',
          dueDate: '',
          studentId: null,
          studentName: null
        };
        this.loadFees();
      },
      error: () => {
        this.statusMessage = 'Could not save the fee record.';
      }
    });
  }

  onStudentChange(studentId: number | null): void {
    this.selectedStudentId = studentId;
    const existingFee = this.fees.find(fee => fee.studentId === studentId);
    this.draftFee = existingFee ? { ...existingFee } : {
      feesId: 0,
      amount: 0,
      feesStatus: '',
      dueDate: '',
      studentId,
      studentName: this.students.find(student => student.studentId === studentId)?.name ?? null
    };
  }
}
