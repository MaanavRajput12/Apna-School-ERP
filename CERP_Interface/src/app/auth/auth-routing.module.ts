import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'student' },
  { path: 'student', component: LoginComponent, data: { role: 'STUDENT' } },
  { path: 'faculty', component: LoginComponent, data: { role: 'FACULTY' } },
  { path: 'admin', component: LoginComponent, data: { role: 'ADMIN' } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {}
