import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserItemDatatableComponent } from './user-item-datatable.component';

describe('UserItemDatatableComponent', () => {
  let component: UserItemDatatableComponent;
  let fixture: ComponentFixture<UserItemDatatableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserItemDatatableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserItemDatatableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
