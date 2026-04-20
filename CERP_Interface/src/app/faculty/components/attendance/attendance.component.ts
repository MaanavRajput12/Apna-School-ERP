import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Attendance, AttendancePayload, Faculty, FacultySchedule, Student, Subject } from '../../../core/models/erp.models';
import { ErpApiService } from '../../../core/services/erp-api.service';
import { FacultySessionService } from '../../services/faculty-session.service';

interface TeachingSlot {
  facultyScheduleId: number;
  subjectId: number;
  subjectName: string;
  courseName: string;
  scheduleTime: string;
  classroom: string;
}

@Component({
  selector: 'app-attendance',
  standalone: false,
  templateUrl: './attendance.component.html',
  styleUrl: './attendance.component.css'
})
export class AttendanceComponent implements OnInit {
  faculty: Faculty | null = null;
  students: Student[] = [];
  subjects: Subject[] = [];
  attendance: Attendance[] = [];
  schedules: FacultySchedule[] = [];
  filteredStudents: Student[] = [];
  teachingSlots: TeachingSlot[] = [];
  statusMessage = '';

  selectedSemester = '';
  selectedDate = new Date().toISOString().slice(0, 10);
  selectedSlotId: number | null = null;

  constructor(
    private readonly api: ErpApiService,
    private readonly facultySession: FacultySessionService
  ) {}

  ngOnInit(): void {
    const facultyId = this.facultySession.getFacultyId();
    if (!facultyId) {
      this.statusMessage = 'Faculty session not found.';
      return;
    }

    forkJoin({
      faculty: this.api.getFacultyById(facultyId),
      students: this.api.getStudents(),
      subjects: this.api.getSubjects(),
      attendance: this.api.getAttendance(),
      schedules: this.api.getFacultySchedules()
    }).subscribe({
      next: ({ faculty, students, subjects, attendance, schedules }) => {
        this.faculty = faculty;
        this.students = students;
        this.subjects = subjects;
        this.attendance = attendance;
        this.schedules = schedules;
        this.refreshView();
      },
      error: () => {
        this.statusMessage = 'Unable to load attendance workspace data.';
      }
    });
  }

  refreshView(): void {
    const department = this.faculty?.department;
    const relatedSubjects = this.subjects.filter(subject => subject.facultyName === this.faculty?.facultyName || !subject.facultyName);

    this.teachingSlots = this.schedules
      .filter(schedule => schedule.departmentName === department)
      .map(schedule => {
        const subject = relatedSubjects.find(item => item.subjectId === schedule.subjectId);
        return {
          facultyScheduleId: schedule.facultyScheduleId,
          subjectId: schedule.subjectId ?? 0,
          subjectName: subject?.name ?? `Subject #${schedule.subjectId}`,
          courseName: subject?.courseName ?? 'Unassigned',
          scheduleTime: schedule.scheduleTime,
          classroom: schedule.classroom
        };
      });

    if (!this.selectedSlotId && this.teachingSlots.length > 0) {
      this.selectedSlotId = this.teachingSlots[0].facultyScheduleId;
    }

    this.filteredStudents = this.students.filter(student => {
      const departmentMatches = department ? student.departmentName === department : true;
      const semesterMatches = this.selectedSemester ? student.semester === this.selectedSemester : true;
      return departmentMatches && semesterMatches;
    });
  }

  markAttendance(student: Student, present: boolean): void {
    const selectedSlot = this.selectedTeachingSlot;
    if (!this.faculty?.facultyId || !selectedSlot) {
      this.statusMessage = 'Select a teaching slot before marking attendance.';
      return;
    }

    const existingRecord = this.attendance.find(entry =>
      entry.studentId === student.studentId &&
      entry.facultyId === this.faculty?.facultyId &&
      entry.subjectId === selectedSlot.subjectId &&
      entry.date === this.selectedDate
    );

    const payload: AttendancePayload = {
      date: this.selectedDate,
      present,
      student: { studentId: student.studentId },
      subject: { subjectId: selectedSlot.subjectId },
      faculty: { facultyId: this.faculty.facultyId }
    };

    const request = existingRecord
      ? this.api.updateAttendance(existingRecord.attendanceId, payload)
      : this.api.createAttendance(payload);

    request.subscribe({
      next: savedRecord => {
        this.attendance = existingRecord
          ? this.attendance.map(item => item.attendanceId === savedRecord.attendanceId ? savedRecord : item)
          : [...this.attendance, savedRecord];
        this.statusMessage = `${student.name} marked as ${present ? 'present' : 'absent'}.`;
      },
      error: () => {
        this.statusMessage = `Could not mark attendance for ${student.name}.`;
      }
    });
  }

  getAttendanceLabel(studentId: number): string {
    const selectedSlot = this.selectedTeachingSlot;
    if (!selectedSlot || !this.faculty?.facultyId) {
      return 'Pending';
    }

    const record = this.attendance.find(entry =>
      entry.studentId === studentId &&
      entry.facultyId === this.faculty?.facultyId &&
      entry.subjectId === selectedSlot.subjectId &&
      entry.date === this.selectedDate
    );

    if (!record) {
      return 'Pending';
    }

    return record.present ? 'Present' : 'Absent';
  }

  get selectedTeachingSlot(): TeachingSlot | undefined {
    return this.teachingSlots.find(slot => slot.facultyScheduleId === this.selectedSlotId);
  }
}
