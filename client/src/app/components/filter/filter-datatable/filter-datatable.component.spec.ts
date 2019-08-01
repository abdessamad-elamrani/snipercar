import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterDatatableComponent } from './filter-datatable.component';

describe('FilterDatatableComponent', () => {
  let component: FilterDatatableComponent;
  let fixture: ComponentFixture<FilterDatatableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilterDatatableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterDatatableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
