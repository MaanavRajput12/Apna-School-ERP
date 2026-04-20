import { Component, OnInit } from '@angular/core';
import { Department } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

type EditableDepartment = Department & { isEditing: boolean };

@Component({
  selector: 'app-department',
  standalone: false,
  templateUrl: './department.component.html',
  styleUrl: './department.component.css'
})
export class DepartmentComponent implements OnInit {
  departments: EditableDepartment[] = [];
  statusMessage = '';

  newDepartment: Partial<Department> = {
    departmentName: '',
    numberOfStudents: 0,
    numberOfFaculties: 0
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadDepartments();
  }

  loadDepartments(): void {
    this.api.getDepartments().subscribe({
      next: departments => {
        this.departments = departments.map(department => ({ ...department, isEditing: false }));
      },
      error: () => {
        this.statusMessage = 'Unable to load departments.';
      }
    });
  }

  addDepartment(): void {
    this.api.createDepartment(this.newDepartment).subscribe({
      next: createdDepartment => {
        this.departments = [{ ...createdDepartment, isEditing: false }, ...this.departments];
        this.newDepartment = {
          departmentName: '',
          numberOfStudents: 0,
          numberOfFaculties: 0
        };
        this.statusMessage = 'Department added successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not add the department.';
      }
    });
  }

  editRow(index: number): void {
    this.departments[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.departments[index];
    this.api.updateDepartment(row.departmentId, {
      departmentName: row.departmentName,
      numberOfStudents: Number(row.numberOfStudents),
      numberOfFaculties: Number(row.numberOfFaculties)
    }).subscribe({
      next: updatedDepartment => {
        this.departments[index] = { ...updatedDepartment, isEditing: false };
        this.statusMessage = 'Department updated successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not update the department.';
      }
    });
  }

  deleteDepartment(department: Department): void {
    this.api.deleteDepartment(department.departmentId).subscribe({
      next: () => {
        this.departments = this.departments.filter(item => item.departmentId !== department.departmentId);
        this.statusMessage = 'Department removed successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not remove the department.';
      }
    });
  }
}
