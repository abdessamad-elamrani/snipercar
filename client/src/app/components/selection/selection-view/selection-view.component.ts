import { AuthService } from './../../../services/auth.service';
import { Selection } from './../../../models/selection';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-selection-view',
  templateUrl: './selection-view.component.html',
  styleUrls: ['./selection-view.component.css']
})
export class SelectionViewComponent implements OnInit {

  selection: Selection;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {
    this.selection = new Selection();
    this.route.params.subscribe(params => {
      this.http.get(
        '/api/selection/' + params['id']
      ).subscribe((selection: Selection) => {
        this.selection = selection;
      });
    });
  }

  ngOnInit() {
  }

}
