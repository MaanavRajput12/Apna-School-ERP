import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { AuthSessionService } from '../../../core/services/auth-session.service';

type LoginRole = 'ADMIN' | 'FACULTY' | 'STUDENT';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  imports: [CommonModule, ReactiveFormsModule]
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorMessage = '';
  expectedRole: LoginRole = 'STUDENT';
  pageTitle = 'Sign in to the student portal.';
  pageSubtitle = 'Continue to the student workspace for attendance, timetable, exams, and fee tracking.';
  roleLabel = 'Student';

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly api: ErpApiService,
    private readonly authSession: AuthSessionService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    const role = (this.route.snapshot.data['role'] as LoginRole | undefined) ?? 'STUDENT';
    this.expectedRole = role;

    if (role === 'ADMIN') {
      this.roleLabel = 'Admin';
      this.pageTitle = 'Sign in to the admin portal.';
      this.pageSubtitle = 'Continue to the admin workspace for students, faculty, schedules, fees, courses, and departments.';
      return;
    }

    if (role === 'FACULTY') {
      this.roleLabel = 'Faculty';
      this.pageTitle = 'Sign in to the faculty portal.';
      this.pageSubtitle = 'Continue to the faculty workspace for attendance, department timetable, and exam schedule.';
      return;
    }
  }

  onSubmit(): void {
    if (!this.loginForm.valid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.errorMessage = '';
    this.api.login(this.loginForm.getRawValue()).subscribe({
      next: response => {
        if (response.role !== this.expectedRole) {
          this.errorMessage = `${this.roleLabel} login requires a ${this.roleLabel.toLowerCase()} account.`;
          return;
        }

        this.authSession.setSession({
          userId: response.userId,
          role: response.role as LoginRole,
          studentId: response.studentId,
          facultyId: response.facultyId,
          adminId: response.adminId
        });

        if (response.role === 'ADMIN') {
          this.router.navigate(['/admin']);
          return;
        }

        if (response.role === 'FACULTY') {
          this.router.navigate(['/faculty']);
          return;
        }

        this.router.navigate(['/students']);
      },
      error: () => {
        this.errorMessage = 'Invalid email or password.';
      }
    });
  }
}
