import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlaEditComponent } from './sla-edit.component';

describe('SlaEditComponent', () => {
  let component: SlaEditComponent;
  let fixture: ComponentFixture<SlaEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlaEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlaEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
