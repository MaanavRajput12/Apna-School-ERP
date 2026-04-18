import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './components/layout/landing/landing.component';
import { AttendanceComponent } from './components/attendance/attendance.component';
import { ExamComponent } from './components/exam/exam.component';
import { FeesComponent } from './components/fees/fees.component';
import { time } from 'console';
import { TimetableComponent } from './components/timetable/timetable.component';

const routes: Routes = [
  {
    path: '',
    component: LandingComponent
  },
  {
    path: 'attendance', component: AttendanceComponent
  },
  {
    path: 'exam', component: ExamComponent
  },
  {
    path: 'fees', component: FeesComponent
  },
  {
    path: 'timetable', component: TimetableComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StudentsRoutingModule { }
