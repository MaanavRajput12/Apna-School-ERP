import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Department, Faculty, Subject, SubjectPayload } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

type EditableSubject = Subject & {
  isEditing: boolean;
  departmentId: number | null;
  facultyId: number | null;
};

@Component({
  selector: 'app-subject',
  standalone: false,
  templateUrl: './subject.component.html',
  styleUrl: './subject.component.css'
})
export class SubjectComponent implements OnInit {
  subjects: EditableSubject[] = [];
  departments: Department[] = [];
  facultyList: Faculty[] = [];
  statusMessage = '';

  get activeSubjects(): EditableSubject[] {
    return this.subjects.filter(subject => subject.active !== false);
  }

  newSubject: {
    name: string;
    departmentId: number | null;
    facultyId: number | null;
  } = {
    name: '',
    departmentId: null,
    facultyId: null
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    forkJoin({
      subjects: this.api.getSubjects(),
      departments: this.api.getDepartments(),
      facultyList: this.api.getFaculty()
    }).subscribe({
      next: ({ subjects, departments, facultyList }) => {
        this.departments = departments;
        this.facultyList = facultyList;
        this.subjects = subjects.map(subject => ({
          ...subject,
          active: subject.active !== false,
          isEditing: false,
          departmentId: this.findDepartmentId(subject.departmentName),
          facultyId: this.findFacultyId(subject.facultyName)
        }));
      },
      error: () => {
        this.statusMessage = 'Unable to load subject data.';
      }
    });
  }

  addSubject(): void {
    const submittedDepartmentId = this.newSubject.departmentId;
    const submittedFacultyId = this.newSubject.facultyId;

    this.api.createSubject(this.buildPayload(this.newSubject)).subscribe({
      next: createdSubject => {
        this.loadData();
        this.newSubject = {
          name: '',
          departmentId: submittedDepartmentId,
          facultyId: submittedFacultyId
        };
        this.statusMessage = 'Subject added successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not add the subject.';
      }
    });
  }

  editRow(subjectId: number): void {
    this.subjects = this.subjects.map(subject =>
      subject.subjectId === subjectId ? { ...subject, isEditing: true } : subject
    );
  }

  saveRow(subjectId: number): void {
    const row = this.subjects.find(subject => subject.subjectId === subjectId);
    if (!row) {
      this.statusMessage = 'Subject row not found.';
      return;
    }

    this.api.updateSubject(row.subjectId, this.buildPayload(row)).subscribe({
      next: updatedSubject => {
        this.subjects = this.subjects.map(subject =>
          subject.subjectId === subjectId
            ? {
                ...updatedSubject,
                active: updatedSubject.active !== false,
                isEditing: false,
                departmentId: this.findDepartmentId(updatedSubject.departmentName),
                facultyId: this.findFacultyId(updatedSubject.facultyName)
              }
            : subject
        );
        this.statusMessage = 'Subject updated successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not update the subject.';
      }
    });
  }

  deleteSubject(subject: Subject): void {
    this.api.deleteSubject(subject.subjectId).subscribe({
      next: () => {
        this.subjects = this.subjects.map(item =>
          item.subjectId === subject.subjectId ? { ...item, active: false } : item
        );
        this.statusMessage = 'Subject archived successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not remove the subject.';
      }
    });
  }

  private buildPayload(subject: {
    name: string;
    departmentId: number | null;
    facultyId: number | null;
  }): SubjectPayload {
    return {
      name: subject.name,
      department: subject.departmentId ? { departmentId: subject.departmentId } : null,
      faculty: subject.facultyId ? { facultyId: subject.facultyId } : null
    };
  }

  private findDepartmentId(departmentName: string | null): number | null {
    return this.departments.find(department => department.departmentName === departmentName)?.departmentId ?? null;
  }

  private findFacultyId(facultyName: string | null): number | null {
    return this.facultyList.find(faculty => faculty.facultyName === facultyName)?.facultyId ?? null;
  }
}
