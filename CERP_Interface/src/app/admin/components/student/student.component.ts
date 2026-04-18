import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Course, Department, Student, StudentPayload } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

@Component({
  selector: 'app-student',
  standalone: false,
  templateUrl: './student.component.html',
  styleUrl: './student.component.css'
})
export class StudentComponent implements OnInit {
  studentList: Array<Student & { isEditing: boolean }> = [];
  departments: Department[] = [];
  courses: Course[] = [];
  statusMessage = '';
  isLoading = false;

  newStudent: StudentPayload = this.createEmptyStudent();

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    forkJoin({
      students: this.api.getStudents(),
      departments: this.api.getDepartments(),
      courses: this.api.getCourses()
    }).subscribe({
      next: ({ students, departments, courses }) => {
        this.studentList = students.map(student => ({ ...student, isEditing: false }));
        this.departments = departments;
        this.courses = courses;
        this.isLoading = false;
      },
      error: () => {
        this.statusMessage = 'Unable to load students right now.';
        this.isLoading = false;
      }
    });
  }

  editRow(index: number): void {
    this.studentList[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.studentList[index];
    const payload = this.toStudentPayload(row);

    if (!payload) {
      this.statusMessage = 'Student department and course must match available master data.';
      return;
    }

    this.api.updateStudent(row.studentId, payload).subscribe({
      next: updatedStudent => {
        this.studentList[index] = { ...updatedStudent, isEditing: false };
        this.statusMessage = `${updatedStudent.name} was updated successfully.`;
      },
      error: () => {
        this.statusMessage = `Could not update ${row.name}. Please verify the data.`;
      }
    });
  }

  addStudent(): void {
    this.api.createStudent(this.newStudent).subscribe({
      next: createdStudent => {
        this.studentList = [{ ...createdStudent, isEditing: false }, ...this.studentList];
        this.newStudent = this.createEmptyStudent();
        this.statusMessage = `${createdStudent.name} was added successfully.`;
      },
      error: () => {
        this.statusMessage = 'Could not add the student. Please check the required fields.';
      }
    });
  }

  deleteStudent(student: Student): void {
    this.api.deleteStudent(student.studentId).subscribe({
      next: () => {
        this.studentList = this.studentList.filter(item => item.studentId !== student.studentId);
        this.statusMessage = `${student.name} was removed successfully.`;
      },
      error: () => {
        this.statusMessage = `Could not remove ${student.name}.`;
      }
    });
  }

  private toStudentPayload(student: Student): StudentPayload | null {
    const departmentId = this.departments.find(department => department.departmentName === student.departmentName)?.departmentId;
    const courseId = this.courses.find(course => course.courseName === student.courseName)?.courseId;

    if (!departmentId || !courseId) {
      return null;
    }

    return {
      name: student.name,
      email: student.email,
      dob: student.dob,
      gender: student.gender,
      rollNo: student.rollNo,
      phoneNo: Number(student.phoneNo),
      address: student.address,
      semester: student.semester,
      departmentId,
      courseId,
      feesId: null,
      userId: student.userId ? Number(student.userId) : null
    };
  }

  private createEmptyStudent(): StudentPayload {
    return {
      name: '',
      email: '',
      dob: '',
      gender: '',
      rollNo: '',
      phoneNo: 0,
      address: '',
      semester: '',
      departmentId: null,
      courseId: null,
      feesId: null,
      userId: null
    };
  }
}
