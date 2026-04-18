import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./core/core.module').then(m => m.CoreModule)
      }
    ]
  },
  {
    path: 'auth',
    loadChildren: () =>
      import('./auth/auth.module').then(m => m.AuthModule),
  },

  {
    path: 'students',
    loadChildren: () =>
      import('./students/students.module').then(m => m.StudentsModule),
  },

  {
    path: 'admin',
    loadChildren: () =>
      import('./admin/admin.module').then(m => m.AdminModule),
  },

  {
    path: 'faculty',
    loadChildren: () =>
      import('./faculty/faculty.module').then(m => m.FacultyModule),
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
