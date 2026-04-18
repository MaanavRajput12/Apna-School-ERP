import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentTTComponent } from './student-tt.component';

describe('StudentTTComponent', () => {
  let component: StudentTTComponent;
  let fixture: ComponentFixture<StudentTTComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StudentTTComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentTTComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
