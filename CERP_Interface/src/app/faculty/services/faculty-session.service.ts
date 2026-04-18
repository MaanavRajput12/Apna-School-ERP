import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class FacultySessionService {
  private readonly storageKey = 'apna-school-faculty-id';

  getFacultyId(): number | null {
    if (typeof localStorage === 'undefined') {
      return null;
    }
    const storedValue = localStorage.getItem(this.storageKey);
    return storedValue ? Number(storedValue) : null;
  }

  setFacultyId(facultyId: number): void {
    if (typeof localStorage === 'undefined') {
      return;
    }
    localStorage.setItem(this.storageKey, String(facultyId));
  }
}
