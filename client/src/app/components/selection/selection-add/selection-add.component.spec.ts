import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlaAddComponent } from './sla-add.component';

describe('SlaAddComponent', () => {
  let component: SlaAddComponent;
  let fixture: ComponentFixture<SlaAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlaAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlaAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
