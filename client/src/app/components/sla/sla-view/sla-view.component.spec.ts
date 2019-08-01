import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlaViewComponent } from './sla-view.component';

describe('SlaViewComponent', () => {
  let component: SlaViewComponent;
  let fixture: ComponentFixture<SlaViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlaViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlaViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
