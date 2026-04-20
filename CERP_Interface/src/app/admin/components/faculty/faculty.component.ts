import { Component, OnInit } from '@angular/core';
import { Department, Faculty, FacultyPayload } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

type EditableFaculty = Faculty & { isEditing: boolean; loginPassword?: string | null };

@Component({
  selector: 'app-faculty',
  standalone: false,
  templateUrl: './faculty.component.html',
  styleUrl: './faculty.component.css'
})
export class FacultyComponent implements OnInit {
  facultyList: EditableFaculty[] = [];
  departments: Department[] = [];
  statusMessage = '';
  isLoading = false;

  newFaculty: FacultyPayload = {
    facultyName: '',
    email: '',
    designation: '',
    department: '',
    phone: 0,
    address: '',
    loginPassword: ''
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadFaculty();
  }

  loadFaculty(): void {
    this.isLoading = true;
    this.api.getFaculty().subscribe({
      next: facultyList => {
        this.facultyList = facultyList.map(faculty => ({ ...faculty, isEditing: false, loginPassword: '' }));
        this.isLoading = false;
      },
      error: () => {
        this.statusMessage = 'Unable to load faculty records right now.';
        this.isLoading = false;
      }
    });

    this.api.getDepartments().subscribe({
      next: departments => {
        this.departments = departments;
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
      address: row.address,
      loginPassword: row.loginPassword?.trim() ? row.loginPassword.trim() : null
    };

    this.api.updateFaculty(row.facultyId, payload).subscribe({
      next: updatedFaculty => {
        this.facultyList[index] = { ...updatedFaculty, isEditing: false, loginPassword: '' };
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
        this.facultyList = [{ ...createdFaculty, isEditing: false, loginPassword: '' }, ...this.facultyList];
        this.newFaculty = {
          facultyName: '',
          email: '',
          designation: '',
          department: '',
          phone: 0,
          address: '',
          loginPassword: ''
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
