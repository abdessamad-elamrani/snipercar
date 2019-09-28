import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Item } from './../../../models/item';

@Component({
  selector: 'app-item-view',
  templateUrl: './item-view.component.html',
  styleUrls: ['./item-view.component.css']
})
export class ItemViewComponent implements OnInit {

  error: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {
    this.error = false;
    this.route.params.subscribe(params => {
      this.http.get(
        '/api/item/' + params['id']
      ).subscribe(
        (item: Item) => {
          console.error('url', item.url);
          window.location.href = item.url;
        },
        (error) => {
          this.error = true;
        }
      );
    });
  }

  ngOnInit() {
  }

}
