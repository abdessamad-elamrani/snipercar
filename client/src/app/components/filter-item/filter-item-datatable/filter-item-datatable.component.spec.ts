import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterItemDatatableComponent } from './filter-item-datatable.component';

describe('FilterItemDatatableComponent', () => {
  let component: FilterItemDatatableComponent;
  let fixture: ComponentFixture<FilterItemDatatableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilterItemDatatableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterItemDatatableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
