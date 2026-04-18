import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { StudentsRoutingModule } from './students-routing.module';
import { LandingComponent } from './components/layout/landing/landing.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { HeaderComponent } from './components/layout/header/header.component';
import { AttendanceComponent } from './components/attendance/attendance.component';
import { ExamComponent } from './components/exam/exam.component';
import { FeesComponent } from './components/fees/fees.component';
import { TimetableComponent } from './components/timetable/timetable.component';


@NgModule({
  declarations: [
    LandingComponent,
    FooterComponent,
    HeaderComponent,
    AttendanceComponent,
    ExamComponent,
    FeesComponent,
    TimetableComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    StudentsRoutingModule
  ]
})
export class StudentsModule { }
