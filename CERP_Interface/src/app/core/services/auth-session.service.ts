import { Injectable } from '@angular/core';

type AuthRole = 'ADMIN' | 'FACULTY' | 'STUDENT' | null;

interface AuthSession {
  userId: number | null;
  role: AuthRole;
  studentId: number | null;
  facultyId: number | null;
  adminId: number | null;
}

@Injectable({ providedIn: 'root' })
export class AuthSessionService {
  private readonly storageKey = 'cerp-auth-session';

  getSession(): AuthSession {
    if (typeof localStorage === 'undefined') {
      return this.emptySession();
    }

    const rawValue = localStorage.getItem(this.storageKey);
    if (!rawValue) {
      return this.emptySession();
    }

    try {
      return { ...this.emptySession(), ...JSON.parse(rawValue) } as AuthSession;
    } catch {
      return this.emptySession();
    }
  }

  setSession(session: Partial<AuthSession>): void {
    if (typeof localStorage === 'undefined') {
      return;
    }
    const nextValue = { ...this.emptySession(), ...this.getSession(), ...session };
    localStorage.setItem(this.storageKey, JSON.stringify(nextValue));
  }

  clear(): void {
    if (typeof localStorage === 'undefined') {
      return;
    }
    localStorage.removeItem(this.storageKey);
  }

  getStudentId(): number | null {
    return this.getSession().studentId;
  }

  getFacultyId(): number | null {
    return this.getSession().facultyId;
  }

  getRole(): AuthRole {
    return this.getSession().role;
  }

  private emptySession(): AuthSession {
    return {
      userId: null,
      role: null,
      studentId: null,
      facultyId: null,
      adminId: null
    };
  }
}
