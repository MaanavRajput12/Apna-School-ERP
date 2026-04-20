import { Injectable } from '@angular/core';
import { AuthSessionService } from '../../core/services/auth-session.service';

@Injectable({ providedIn: 'root' })
export class FacultySessionService {
  constructor(private readonly authSession: AuthSessionService) {}

  getFacultyId(): number | null {
    return this.authSession.getFacultyId();
  }

  setFacultyId(facultyId: number): void {
    this.authSession.setSession({ facultyId });
  }
}
