import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import {
  Attendance,
  AttendancePayload,
  Course,
  CoursePayload,
  Department,
  Exam,
  ExamPayload,
  Faculty,
  FacultyPayload,
  FacultySchedule,
  FacultySchedulePayload,
  Fee,
  FeePayload,
  LoginRequest,
  LoginResponse,
  Student,
  StudentPayload,
  Subject,
  Timetable,
  TimetablePayload
} from '../models/erp.models';

@Injectable({ providedIn: 'root' })
export class ErpApiService {
  private readonly baseUrl = 'http://localhost:8080';
  private readonly platformId = inject(PLATFORM_ID);

  constructor(private readonly http: HttpClient) {}

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, payload);
  }

  getStudents(): Observable<Student[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Student[]>(`${this.baseUrl}/students`);
  }

  getStudent(studentId: number): Observable<Student> {
    return this.http.get<Student>(`${this.baseUrl}/students/${studentId}`);
  }

  createStudent(payload: StudentPayload): Observable<Student> {
    return this.http.post<Student>(`${this.baseUrl}/students`, payload);
  }

  updateStudent(studentId: number, payload: StudentPayload): Observable<Student> {
    return this.http.put<Student>(`${this.baseUrl}/students/${studentId}`, payload);
  }

  deleteStudent(studentId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/students/${studentId}`);
  }

  getFaculty(): Observable<Faculty[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Faculty[]>(`${this.baseUrl}/faculty`);
  }

  getFacultyById(facultyId: number): Observable<Faculty> {
    return this.http.get<Faculty>(`${this.baseUrl}/faculty/${facultyId}`);
  }

  createFaculty(payload: FacultyPayload): Observable<Faculty> {
    return this.http.post<Faculty>(`${this.baseUrl}/faculty`, payload);
  }

  updateFaculty(facultyId: number, payload: FacultyPayload): Observable<Faculty> {
    return this.http.put<Faculty>(`${this.baseUrl}/faculty/${facultyId}`, payload);
  }

  deleteFaculty(facultyId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/faculty/${facultyId}`);
  }

  getDepartments(): Observable<Department[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Department[]>(`${this.baseUrl}/departments`);
  }

  createDepartment(payload: Partial<Department>): Observable<Department> {
    return this.http.post<Department>(`${this.baseUrl}/departments`, payload);
  }

  updateDepartment(departmentId: number, payload: Partial<Department>): Observable<Department> {
    return this.http.put<Department>(`${this.baseUrl}/departments/${departmentId}`, payload);
  }

  deleteDepartment(departmentId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/departments/${departmentId}`);
  }

  getCourses(): Observable<Course[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Course[]>(`${this.baseUrl}/course`);
  }

  createCourse(payload: CoursePayload): Observable<Course> {
    return this.http.post<Course>(`${this.baseUrl}/course`, payload);
  }

  updateCourse(courseId: number, payload: CoursePayload): Observable<Course> {
    return this.http.put<Course>(`${this.baseUrl}/course/${courseId}`, payload);
  }

  deleteCourse(courseId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/course/${courseId}`);
  }

  getSubjects(): Observable<Subject[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Subject[]>(`${this.baseUrl}/subjects`);
  }

  getAttendance(): Observable<Attendance[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Attendance[]>(`${this.baseUrl}/attendance`);
  }

  createAttendance(payload: AttendancePayload): Observable<Attendance> {
    return this.http.post<Attendance>(`${this.baseUrl}/attendance`, payload);
  }

  updateAttendance(attendanceId: number, payload: AttendancePayload): Observable<Attendance> {
    return this.http.put<Attendance>(`${this.baseUrl}/attendance/${attendanceId}`, payload);
  }

  getTimetables(): Observable<Timetable[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Timetable[]>(`${this.baseUrl}/timetables`);
  }

  createTimetable(payload: TimetablePayload): Observable<Timetable> {
    return this.http.post<Timetable>(`${this.baseUrl}/timetables`, payload);
  }

  updateTimetable(timeTableId: number, payload: TimetablePayload): Observable<Timetable> {
    return this.http.put<Timetable>(`${this.baseUrl}/timetables/${timeTableId}`, payload);
  }

  getFacultySchedules(): Observable<FacultySchedule[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<FacultySchedule[]>(`${this.baseUrl}/facultySchedule`);
  }

  createFacultySchedule(payload: FacultySchedulePayload): Observable<FacultySchedule> {
    return this.http.post<FacultySchedule>(`${this.baseUrl}/facultySchedule`, payload);
  }

  updateFacultySchedule(facultyScheduleId: number, payload: FacultySchedulePayload): Observable<FacultySchedule> {
    return this.http.put<FacultySchedule>(`${this.baseUrl}/facultySchedule/${facultyScheduleId}`, payload);
  }

  getExams(): Observable<Exam[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Exam[]>(`${this.baseUrl}/exams`);
  }

  createExam(payload: ExamPayload): Observable<Exam> {
    return this.http.post<Exam>(`${this.baseUrl}/exams`, payload);
  }

  updateExam(examId: number, payload: ExamPayload): Observable<Exam> {
    return this.http.put<Exam>(`${this.baseUrl}/exams/${examId}`, payload);
  }

  deleteExam(examId: number): Observable<unknown> {
    return this.http.delete(`${this.baseUrl}/exams/${examId}`, { responseType: 'text' as 'json' });
  }

  getFees(): Observable<Fee[]> {
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<Fee[]>(`${this.baseUrl}/api/fees`);
  }

  createFee(payload: FeePayload): Observable<Fee> {
    return this.http.post<Fee>(`${this.baseUrl}/api/fees`, payload);
  }

  updateFee(feesId: number, payload: FeePayload): Observable<Fee> {
    return this.http.put<Fee>(`${this.baseUrl}/api/fees/${feesId}`, payload);
  }
}
