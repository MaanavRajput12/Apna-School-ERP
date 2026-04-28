import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AdminRoutingModule } from './admin-routing.module';
import { LandingComponent } from './components/layout/landing/landing.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { HeaderComponent } from './components/layout/header/header.component';
import { StudentComponent } from './components/student/student.component';
import { FacultyComponent } from './components/faculty/faculty.component';
import { FacultyTTComponent } from './components/faculty-tt/faculty-tt.component';
import { StudentTTComponent } from './components/student-tt/student-tt.component';
import { ExamTTComponent } from './components/exam-tt/exam-tt.component';
import { StudentfeesComponent } from './components/studentfees/studentfees.component';
import { CourseComponent } from './components/course/course.component';
import { DepartmentComponent } from './components/department/department.component';
import { SubjectComponent } from './components/subject/subject.component';


@NgModule({
  declarations: [
    LandingComponent,
    FooterComponent,
    HeaderComponent,
    StudentComponent,
    FacultyComponent,
    FacultyTTComponent,
    StudentTTComponent,
    ExamTTComponent,
    StudentfeesComponent,
    CourseComponent,
    DepartmentComponent,
    SubjectComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule
  ]
})
export class AdminModule { }
