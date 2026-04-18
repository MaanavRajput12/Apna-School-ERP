import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamTTComponent } from './exam-tt.component';

describe('ExamTTComponent', () => {
  let component: ExamTTComponent;
  let fixture: ComponentFixture<ExamTTComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ExamTTComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamTTComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
