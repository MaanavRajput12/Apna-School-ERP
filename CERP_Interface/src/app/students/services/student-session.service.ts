import { Injectable } from '@angular/core';
import { AuthSessionService } from '../../core/services/auth-session.service';

@Injectable({ providedIn: 'root' })
export class StudentSessionService {
  constructor(private readonly authSession: AuthSessionService) {}

  getStudentId(): number | null {
    return this.authSession.getStudentId();
  }

  setStudentId(studentId: number): void {
    this.authSession.setSession({ studentId });
  }
}
