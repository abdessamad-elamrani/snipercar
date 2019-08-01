import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterAddComponent } from './filter-add.component';

describe('FilterAddComponent', () => {
  let component: FilterAddComponent;
  let fixture: ComponentFixture<FilterAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilterAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
