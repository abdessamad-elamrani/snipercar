
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER, LOCALE_ID } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { PNotifyService } from './services/pnotify.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MalihuScrollbarModule } from 'ngx-malihu-scrollbar';
import { DataTablesModule } from 'angular-datatables';
import { Select2Module } from 'ng2-select2';
import { IonRangeSliderModule } from 'ng2-ion-range-slider';
import { OwlDateTimeModule, OwlNativeDateTimeModule, OwlDateTimeIntl, OWL_DATE_TIME_LOCALE } from 'ng-pick-datetime';

import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { ErrorInterceptor } from './interceptors/error.interceptor';
import { ItemListComponent } from './components/item/item-list/item-list.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { ErrorComponent } from './components/error/error.component';
import { AdminDatatableComponent } from './components/admin/admin-datatable/admin-datatable.component';
import { AdminEditComponent } from './components/admin/admin-edit/admin-edit.component';
import { AdminAddComponent } from './components/admin/admin-add/admin-add.component';
import { AdminViewComponent } from './components/admin/admin-view/admin-view.component';
import { FilterDatatableComponent } from './components/filter/filter-datatable/filter-datatable.component';
import { FilterEditComponent } from './components/filter/filter-edit/filter-edit.component';
import { FilterAddComponent } from './components/filter/filter-add/filter-add.component';
import { FilterViewComponent } from './components/filter/filter-view/filter-view.component';
import { SlaDatatableComponent } from './components/sla/sla-datatable/sla-datatable.component';
import { SlaEditComponent } from './components/sla/sla-edit/sla-edit.component';
import { SlaAddComponent } from './components/sla/sla-add/sla-add.component';
import { SlaViewComponent } from './components/sla/sla-view/sla-view.component';
import { CompanyDatatableComponent } from './components/company/company-datatable/company-datatable.component';
import { CompanyEditComponent } from './components/company/company-edit/company-edit.component';
import { CompanyAddComponent } from './components/company/company-add/company-add.component';
import { CompanyViewComponent } from './components/company/company-view/company-view.component';
import { AgentDatatableComponent } from './components/agent/agent-datatable/agent-datatable.component';
import { AgentEditComponent } from './components/agent/agent-edit/agent-edit.component';
import { AgentAddComponent } from './components/agent/agent-add/agent-add.component';
import { AgentViewComponent } from './components/agent/agent-view/agent-view.component';
import { AgentDashboardComponent } from './components/agent/agent-dashboard/agent-dashboard.component';
import { ExtraComponent } from './components/extra/extra.component';

import { NgxPermissionsModule, NgxPermissionsService, NgxPermissionsGuard } from 'ngx-permissions';

export class DefaultIntl extends OwlDateTimeIntl {
  upSecondLabel = 'Ajouter une seconde';
  downSecondLabel = 'Moins une seconde';
  upMinuteLabel = 'Ajouter une minute';
  downMinuteLabel = 'Moins une minute';
  upHourLabel = 'Ajouter une heure';
  downHourLabel = 'Moins une heure';
  prevMonthLabel = 'Le mois précédent';
  nextMonthLabel = 'Le mois prochain';
  prevYearLabel = 'Année précédente';
  nextYearLabel = 'L\'année prochaine';
  prevMultiYearLabel = '21 dernières années';
  nextMultiYearLabel = '21 prochaines années';
  switchToMonthViewLabel = 'Passer à l\'affichage par mois';
  switchToMultiYearViewLabel = 'Choisissez le mois et l\'année';
  cancelBtnLabel = 'Annuler';
  setBtnLabel = 'Mettre';
  rangeFromLabel = 'De';
  rangeToLabel = 'À';
  hour12AMLabel = 'AM';
  hour12PMLabel = 'PM';
}

@NgModule({
  declarations: [
    AppComponent,
    ItemListComponent,
    SidebarComponent,
    HeaderComponent,
    HomeComponent,
    LoginComponent,
    ErrorComponent,
    AdminDatatableComponent,
    AdminEditComponent,
    AdminAddComponent,
    AdminViewComponent,
    FilterDatatableComponent,
    FilterEditComponent,
    FilterAddComponent,
    FilterViewComponent,
    SlaDatatableComponent,
    SlaEditComponent,
    SlaAddComponent,
    SlaViewComponent,
    CompanyDatatableComponent,
    CompanyEditComponent,
    CompanyAddComponent,
    CompanyViewComponent,
    AgentDatatableComponent,
    AgentEditComponent,
    AgentAddComponent,
    AgentViewComponent,
    AgentDashboardComponent,
    ExtraComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    AppRoutingModule,
    MalihuScrollbarModule.forRoot(),
    HttpClientModule,
    DataTablesModule,
    Select2Module,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
    IonRangeSliderModule,
    NgxPermissionsModule.forRoot(),
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: OwlDateTimeIntl, useClass: DefaultIntl },
    {
      provide: LOCALE_ID,
      useValue: 'en-EN'
    },
    PNotifyService,
    // DictionaryService,
    // {
    //   provide: APP_INITIALIZER,
    //   useFactory: (ds: DictionaryService, ps: NgxPermissionsService) => function () {
    //     return ds.load().then((data) => {
    //       return ps.loadPermissions(data)
    //     })
    //   },
    //   deps: [LoadService, NgxPermissionsService],
    //   multi: true
    // }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
