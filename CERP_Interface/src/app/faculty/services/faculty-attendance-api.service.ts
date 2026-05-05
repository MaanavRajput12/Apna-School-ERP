import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  Attendance,
  AttendanceMarkRequest,
  AttendancePercentageResponse,
  Department,
  DepartmentAttendanceStudent,
  Faculty
  ,
  Subject
} from '../../core/models/erp.models';

@Injectable({ providedIn: 'root' })
export class FacultyAttendanceApiService {
  private readonly baseUrl = environment.apiBaseUrl;

  constructor(private readonly http: HttpClient) {}

  getDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(`${this.baseUrl}/departments`);
  }

  getFacultyById(facultyId: number): Observable<Faculty> {
    return this.http.get<Faculty>(`${this.baseUrl}/faculty/${facultyId}`);
  }

  getSubjects(): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.baseUrl}/subjects`);
  }

  getStudentsByDepartment(department: string): Observable<DepartmentAttendanceStudent[]> {
    return this.http.get<DepartmentAttendanceStudent[]>(`${this.baseUrl}/api/students?department=${encodeURIComponent(department)}`);
  }

  markAttendance(payload: AttendanceMarkRequest[]): Observable<Attendance[]> {
    return this.http.post<Attendance[]>(`${this.baseUrl}/api/attendance/mark`, payload);
  }

  getAttendancePercentage(studentId: number): Observable<AttendancePercentageResponse> {
    return this.http.get<AttendancePercentageResponse>(`${this.baseUrl}/api/attendance/percentage/${studentId}`);
  }
}
