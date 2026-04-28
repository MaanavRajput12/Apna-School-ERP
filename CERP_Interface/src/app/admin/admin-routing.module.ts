import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './components/layout/landing/landing.component';
import { StudentComponent } from './components/student/student.component';
import { FacultyComponent } from './components/faculty/faculty.component';
import { StudentTTComponent } from './components/student-tt/student-tt.component';
import { FacultyTTComponent } from './components/faculty-tt/faculty-tt.component';
import { StudentfeesComponent } from './components/studentfees/studentfees.component';
import { ExamTTComponent } from './components/exam-tt/exam-tt.component';
import { CourseComponent } from './components/course/course.component';
import { DepartmentComponent } from './components/department/department.component';
import { SubjectComponent } from './components/subject/subject.component';

const routes: Routes = [
  {
  path: '',
  component: LandingComponent
},
{
  path: "students",
  component: StudentComponent
},
{
  path: "faculty",
  component: FacultyComponent
},
{
  path:"studenttimetable",
  component: StudentTTComponent
},
{
  path: "facultytimetable",
  component: FacultyTTComponent
},
{
  path: "studentfees",
  component: StudentfeesComponent
},
{
  path: "courses",
  component: CourseComponent
},
{
  path: "departments",
  component: DepartmentComponent
},
{
  path: "subjects",
  component: SubjectComponent
},
{
  path: "examtimetable",
  component: ExamTTComponent
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
