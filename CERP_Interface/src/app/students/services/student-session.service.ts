import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class StudentSessionService {
  private readonly storageKey = 'apna-school-student-id';

  getStudentId(): number | null {
    if (typeof localStorage === 'undefined') {
      return null;
    }
    const storedValue = localStorage.getItem(this.storageKey);
    return storedValue ? Number(storedValue) : null;
  }

  setStudentId(studentId: number): void {
    if (typeof localStorage === 'undefined') {
      return;
    }
    localStorage.setItem(this.storageKey, String(studentId));
  }
}
