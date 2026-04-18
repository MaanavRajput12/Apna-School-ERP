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
  facultyList: Faculty[] = [];
  students: Student[] = [];
  subjects: Subject[] = [];
  attendance: Attendance[] = [];
  schedules: FacultySchedule[] = [];
  filteredStudents: Student[] = [];
  teachingSlots: TeachingSlot[] = [];
  statusMessage = '';

  selectedFacultyId: number | null = null;
  selectedSemester = '';
  selectedDate = new Date().toISOString().slice(0, 10);
  selectedSlotId: number | null = null;

  constructor(
    private readonly api: ErpApiService,
    private readonly facultySession: FacultySessionService
  ) {}

  ngOnInit(): void {
    this.selectedFacultyId = this.facultySession.getFacultyId();
    this.loadData();
  }

  loadData(): void {
    forkJoin({
      facultyList: this.api.getFaculty(),
      students: this.api.getStudents(),
      subjects: this.api.getSubjects(),
      attendance: this.api.getAttendance(),
      schedules: this.api.getFacultySchedules()
    }).subscribe({
      next: ({ facultyList, students, subjects, attendance, schedules }) => {
        this.facultyList = facultyList;
        this.students = students;
        this.subjects = subjects;
        this.attendance = attendance;
        this.schedules = schedules;
        if (!this.selectedFacultyId && facultyList.length > 0) {
          this.onFacultyChange(facultyList[0].facultyId);
        } else {
          this.refreshView();
        }
      },
      error: () => {
        this.statusMessage = 'Unable to load attendance workspace data.';
      }
    });
  }

  onFacultyChange(facultyId: number | null): void {
    this.selectedFacultyId = facultyId;
    if (facultyId) {
      this.facultySession.setFacultyId(facultyId);
    }
    this.refreshView();
  }

  refreshView(): void {
    const faculty = this.selectedFaculty;
    const relatedSubjects = this.subjects.filter(subject => subject.facultyName === faculty?.facultyName);
    this.teachingSlots = this.schedules
      .filter(schedule => schedule.facultyId === this.selectedFacultyId)
      .map(schedule => {
        const subject = relatedSubjects.find(item => item.subjectId === schedule.subjectId);
        return {
          facultyScheduleId: schedule.facultyScheduleId,
          subjectId: schedule.subjectId,
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
      const departmentMatches = faculty ? student.departmentName === faculty.department : true;
      const semesterMatches = this.selectedSemester ? student.semester === this.selectedSemester : true;
      return departmentMatches && semesterMatches;
    });
  }

  markAttendance(student: Student, present: boolean): void {
    const selectedSlot = this.selectedTeachingSlot;
    if (!this.selectedFacultyId || !selectedSlot) {
      this.statusMessage = 'Select faculty and a teaching slot before marking attendance.';
      return;
    }

    const existingRecord = this.attendance.find(entry =>
      entry.studentId === student.studentId &&
      entry.facultyId === this.selectedFacultyId &&
      entry.subjectId === selectedSlot.subjectId &&
      entry.date === this.selectedDate
    );

    const payload: AttendancePayload = {
      date: this.selectedDate,
      present,
      student: { studentId: student.studentId },
      subject: { subjectId: selectedSlot.subjectId },
      faculty: { facultyId: this.selectedFacultyId }
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
    if (!selectedSlot || !this.selectedFacultyId) {
      return 'Pending';
    }

    const record = this.attendance.find(entry =>
      entry.studentId === studentId &&
      entry.facultyId === this.selectedFacultyId &&
      entry.subjectId === selectedSlot.subjectId &&
      entry.date === this.selectedDate
    );

    if (!record) {
      return 'Pending';
    }

    return record.present ? 'Present' : 'Absent';
  }

  get selectedFaculty(): Faculty | undefined {
    return this.facultyList.find(faculty => faculty.facultyId === this.selectedFacultyId);
  }

  get selectedTeachingSlot(): TeachingSlot | undefined {
    return this.teachingSlots.find(slot => slot.facultyScheduleId === this.selectedSlotId);
  }
}
