import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.css']
})
export class ItemListComponent implements OnInit {

  items = [];
  filterId: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {
    this.route.params.subscribe(params => {
      this.filterId = params['id'];
      this.getItems();
    });
  }

  ngOnInit() {
  }

  getItems() {
    this.http.get(
      '/api/v1/items/' + this.filterId
    ).subscribe((items: any[]) => {
      this.items = items;
    });
  }

}
