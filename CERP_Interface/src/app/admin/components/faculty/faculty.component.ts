import { Component, OnInit } from '@angular/core';
import { Faculty, FacultyPayload } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

@Component({
  selector: 'app-faculty',
  standalone: false,
  templateUrl: './faculty.component.html',
  styleUrl: './faculty.component.css'
})
export class FacultyComponent implements OnInit {
  facultyList: Array<Faculty & { isEditing: boolean }> = [];
  statusMessage = '';
  isLoading = false;

  newFaculty: FacultyPayload = {
    facultyName: '',
    email: '',
    designation: '',
    department: '',
    phone: 0,
    address: ''
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadFaculty();
  }

  loadFaculty(): void {
    this.isLoading = true;
    this.api.getFaculty().subscribe({
      next: facultyList => {
        this.facultyList = facultyList.map(faculty => ({ ...faculty, isEditing: false }));
        this.isLoading = false;
      },
      error: () => {
        this.statusMessage = 'Unable to load faculty records right now.';
        this.isLoading = false;
      }
    });
  }

  editRow(index: number): void {
    this.facultyList[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.facultyList[index];
    const payload: FacultyPayload = {
      facultyName: row.facultyName,
      email: row.email,
      designation: row.designation,
      department: row.department,
      phone: Number(row.phone),
      address: row.address
    };

    this.api.updateFaculty(row.facultyId, payload).subscribe({
      next: updatedFaculty => {
        this.facultyList[index] = { ...updatedFaculty, isEditing: false };
        this.statusMessage = `${updatedFaculty.facultyName} was updated successfully.`;
      },
      error: () => {
        this.statusMessage = `Could not update ${row.facultyName}.`;
      }
    });
  }

  addFaculty(): void {
    this.api.createFaculty(this.newFaculty).subscribe({
      next: createdFaculty => {
        this.facultyList = [{ ...createdFaculty, isEditing: false }, ...this.facultyList];
        this.newFaculty = {
          facultyName: '',
          email: '',
          designation: '',
          department: '',
          phone: 0,
          address: ''
        };
        this.statusMessage = `${createdFaculty.facultyName} was added successfully.`;
      },
      error: () => {
        this.statusMessage = 'Could not add the faculty record.';
      }
    });
  }

  deleteFaculty(faculty: Faculty): void {
    this.api.deleteFaculty(faculty.facultyId).subscribe({
      next: () => {
        this.facultyList = this.facultyList.filter(item => item.facultyId !== faculty.facultyId);
        this.statusMessage = `${faculty.facultyName} was removed successfully.`;
      },
      error: () => {
        this.statusMessage = `Could not remove ${faculty.facultyName}.`;
      }
    });
  }

}
