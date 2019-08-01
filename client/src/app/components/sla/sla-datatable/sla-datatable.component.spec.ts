import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlaDatatableComponent } from './sla-datatable.component';

describe('SlaDatatableComponent', () => {
  let component: SlaDatatableComponent;
  let fixture: ComponentFixture<SlaDatatableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlaDatatableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlaDatatableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
