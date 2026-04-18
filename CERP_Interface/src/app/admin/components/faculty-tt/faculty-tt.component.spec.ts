import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacultyTTComponent } from './faculty-tt.component';
import { beforeEach, describe, it } from 'node:test';

describe('FacultyTTComponent', () => {
  let component: FacultyTTComponent;
  let fixture: ComponentFixture<FacultyTTComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FacultyTTComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacultyTTComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
