import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-agent-dashboard',
  templateUrl: './agent-dashboard.component.html',
  styleUrls: ['./agent-dashboard.component.css']
})
export class AgentDashboardComponent implements OnInit {
  public emailActive = false;
  public smsActive = false;
  public selectedAll = false;
  public selectedMulti = false;
  public filterData = [];
  public filterAddData = [];
  public filterEditData = [];
  constructor() { }

  ngOnInit() {
    this.filterData = [
      { id: 1, name: 'Filter 1' },
      { id: 2, name: 'Filter 2' },
      { id: 3, name: 'Filter 3' },
      { id: 4, name: 'Filter 4' },
      { id: 5, name: 'Filter 5' },
      { id: 6, name: 'Filter 6' }
    ];
    this.filterAddData = [
      { id: 1, name: 'Filter 1' },
    ];
    this.filterEditData = [
      { id: 1, name: 'Filter 1' },
    ];
  }

  smsToggle() {
    this.smsActive = !this.smsActive;
  }
  emailToggle() {
    this.emailActive = !this.emailActive;
  }

  onSelectedAllChange() {
    $('[name="checkboxs[]"]:enabled').prop('checked', this.selectedAll);
    this.onCheckChange();
  }
  onCheckChange() {
    const length = $('[name="checkboxs[]"]:checked').length;
    if (length > 1) {
      this.selectedMulti = true;
      this.selectedAll = true;
    } else {
      this.selectedMulti = false;
      this.selectedAll = false;
    }
  }

  RowSelected(ev: any) {
    $('.selection .card').removeClass('over');
    $(ev.target).closest('.card').addClass('over');
  }

  addNewFilter() {
    this.filterAddData.push(
      {
        id: this.filterAddData.length + 1,
        name: 'Filter ' + (this.filterAddData.length + 1)
      }
    );
  }
  editNewFilter() {
    this.filterEditData.push(
      {
        id: this.filterEditData.length + 1,
        name: 'Filter ' + (this.filterEditData.length + 1)
      }
    );
  }

  removeAddFilter(index) {
    this.filterAddData.splice(index, 1);
  }
  removeEditFilter(index) {
    this.filterEditData.splice(index, 1);
  }
}
