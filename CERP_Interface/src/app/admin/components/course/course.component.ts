import { Component, OnInit } from '@angular/core';
import { Course, CoursePayload } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';

type EditableCourse = Course & { isEditing: boolean };

@Component({
  selector: 'app-course',
  standalone: false,
  templateUrl: './course.component.html',
  styleUrl: './course.component.css'
})
export class CourseComponent implements OnInit {
  courses: EditableCourse[] = [];
  statusMessage = '';

  newCourse: CoursePayload = {
    courseName: '',
    credits: 0,
    department: null
  };

  constructor(private readonly api: ErpApiService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.api.getCourses().subscribe({
      next: courses => {
        this.courses = courses.map(course => ({ ...course, isEditing: false }));
      },
      error: () => {
        this.statusMessage = 'Unable to load courses.';
      }
    });
  }

  addCourse(): void {
    this.api.createCourse({
      courseName: this.newCourse.courseName,
      credits: Number(this.newCourse.credits),
      department: null
    }).subscribe({
      next: createdCourse => {
        this.courses = [{ ...createdCourse, isEditing: false }, ...this.courses];
        this.newCourse = {
          courseName: '',
          credits: 0,
          department: null
        };
        this.statusMessage = 'Course added successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not add the course.';
      }
    });
  }

  editRow(index: number): void {
    this.courses[index].isEditing = true;
  }

  saveRow(index: number): void {
    const row = this.courses[index];
    this.api.updateCourse(row.courseId, {
      courseName: row.courseName,
      credits: Number(row.credits),
      department: null
    }).subscribe({
      next: updatedCourse => {
        this.courses[index] = { ...updatedCourse, isEditing: false };
        this.statusMessage = 'Course updated successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not update the course.';
      }
    });
  }

  deleteCourse(course: Course): void {
    this.api.deleteCourse(course.courseId).subscribe({
      next: () => {
        this.courses = this.courses.filter(item => item.courseId !== course.courseId);
        this.statusMessage = 'Course removed successfully.';
      },
      error: () => {
        this.statusMessage = 'Could not remove the course.';
      }
    });
  }
}
