import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyDatatableComponent } from './company-datatable.component';

describe('CompanyDatatableComponent', () => {
  let component: CompanyDatatableComponent;
  let fixture: ComponentFixture<CompanyDatatableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanyDatatableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyDatatableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
