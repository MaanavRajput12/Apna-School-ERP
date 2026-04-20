export interface Student {
  studentId: number;
  name: string;
  email: string;
  dob: string;
  gender: string;
  rollNo: string;
  phoneNo: number;
  address: string;
  semester: string;
  departmentName: string | null;
  feesStatus: string | null;
  feesId: number | null;
  courseName: string | null;
  userId: number | null;
  active?: boolean;
}

export interface StudentPayload {
  name: string;
  email: string;
  dob: string;
  gender: string;
  rollNo: string;
  phoneNo: number;
  address: string;
  semester: string;
  feesId?: number | null;
  courseId?: number | null;
  departmentId?: number | null;
  userId?: number | null;
  loginPassword?: string | null;
}

export interface Faculty {
  facultyId: number;
  facultyName: string;
  email: string;
  designation: string;
  department: string;
  phone: number;
  address: string;
  userId: number | null;
}

export interface FacultyPayload {
  facultyName: string;
  email: string;
  designation: string;
  department: string;
  phone: number;
  address: string;
  userId?: number | null;
  loginPassword?: string | null;
}

export interface Department {
  departmentId: number;
  departmentName: string;
  numberOfStudents: number;
  numberOfFaculties: number;
}

export interface Course {
  courseId: number;
  courseName: string;
  credits: number;
  departmentId: number | null;
  departmentName: string | null;
}

export interface CoursePayload {
  courseName: string;
  credits: number;
  department?: { departmentId: number } | null;
}

export interface Subject {
  subjectId: number;
  name: string;
  syllabus: string;
  courseName: string | null;
  facultyName: string | null;
}

export interface Attendance {
  attendanceId: number;
  studentId: number | null;
  date: string;
  present: boolean;
  subjectId: number | null;
  facultyId: number | null;
}

export interface AttendancePayload {
  date: string;
  present: boolean;
  student: { studentId: number };
  subject: { subjectId: number };
  faculty: { facultyId: number };
}

export interface Timetable {
  timeTableId: number;
  semester: number;
  day: string;
  timeSlot: string;
  departmentName: string | null;
  facultyName: string | null;
}

export interface TimetablePayload {
  semester: number;
  day: string;
  timeSlot: string;
  department: { departmentId: number };
  faculty: { facultyId: number };
}

export interface FacultySchedule {
  facultyScheduleId: number;
  facultyId: number | null;
  departmentId: number | null;
  departmentName: string | null;
  subjectId: number | null;
  scheduleTime: string;
  classroom: string;
}

export interface FacultySchedulePayload {
  faculty?: { facultyId: number } | null;
  department?: { departmentId: number } | null;
  subject?: { subjectId: number } | null;
  scheduleTime: string;
  classroom: string;
}

export interface Exam {
  examId: number;
  examType: string;
  examDate: string;
  examTime: string;
  totalMarks: number;
  subjectName: string;
}

export interface ExamPayload {
  examType: string;
  examDate: string;
  examTime: string;
  totaMarks: number;
  subjectName: string;
  subject?: { subjectId: number } | null;
}

export interface Fee {
  feesId: number;
  amount: number;
  feesStatus: string;
  dueDate: string;
  studentId: number | null;
  studentName: string | null;
}

export interface FeePayload {
  amount: number;
  feesStatus: string;
  dueDate: string;
  studentId?: number | null;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  userId: number;
  role: string;
  studentId: number | null;
  facultyId: number | null;
  adminId: number | null;
}
