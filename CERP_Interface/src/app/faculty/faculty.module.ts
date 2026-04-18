import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { FacultyRoutingModule } from './faculty-routing.module';
import { HeaderComponent } from './components/layout/header/header.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { LandingComponent } from './components/layout/landing/landing.component';
import { AttendanceComponent } from './components/attendance/attendance.component';
import { FacultyScheduleComponent } from './components/faculty-schedule/faculty-schedule.component';
import { StudentScheduleComponent } from './components/student-schedule/student-schedule.component';
import { ExamScheduleComponent } from './components/exam-schedule/exam-schedule.component';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    LandingComponent,
    AttendanceComponent,
    FacultyScheduleComponent,
    StudentScheduleComponent,
    ExamScheduleComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    FacultyRoutingModule
  ]
})
export class FacultyModule { }
