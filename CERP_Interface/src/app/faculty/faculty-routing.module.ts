import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './components/layout/landing/landing.component';
import { AttendanceComponent } from './components/attendance/attendance.component';
import { FacultyScheduleComponent } from './components/faculty-schedule/faculty-schedule.component';
import { StudentScheduleComponent } from './components/student-schedule/student-schedule.component';
import { ExamScheduleComponent } from './components/exam-schedule/exam-schedule.component';

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'attendance', component: AttendanceComponent },
  { path: 'faculty-schedule', component: FacultyScheduleComponent },
  { path: 'student-schedule', component: StudentScheduleComponent },
  { path: 'exam-schedule', component: ExamScheduleComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FacultyRoutingModule { }
