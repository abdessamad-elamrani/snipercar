import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentDatatableComponent } from './agent-datatable.component';

describe('AgentDatatableComponent', () => {
  let component: AgentDatatableComponent;
  let fixture: ComponentFixture<AgentDatatableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgentDatatableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgentDatatableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
