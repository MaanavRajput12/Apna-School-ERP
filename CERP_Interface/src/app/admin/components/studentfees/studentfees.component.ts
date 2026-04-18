import { Component, OnInit } from '@angular/core';
import { Fee } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

@Component({
  selector: 'app-studentfees',
  standalone: false,
  templateUrl: './studentfees.component.html',
  styleUrl: './studentfees.component.css'
})
export class StudentfeesComponent implements OnInit {
  fees: Array<Fee & { isEditing: boolean }> = [];
  statusMessage = '';

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadFees();
  }

  loadFees(): void {
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
    this.fees[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.fees[index];
    this.api.updateFee(row.feesId, {
      amount: Number(row.amount),
      feesStatus: row.feesStatus,
      dueDate: row.dueDate
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
}
