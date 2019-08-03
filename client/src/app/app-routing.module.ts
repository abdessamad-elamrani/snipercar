import { ExtraComponent } from './components/extra/extra.component';
import { ErrorComponent } from './components/error/error.component';
import { LoginComponent } from './components/login/login.component';
import { AgentDashboardComponent } from './components/agent/agent-dashboard/agent-dashboard.component';
import { SlaAddComponent } from './components/sla/sla-add/sla-add.component';
import { SlaEditComponent } from './components/sla/sla-edit/sla-edit.component';
import { SlaViewComponent } from './components/sla/sla-view/sla-view.component';
import { SlaDatatableComponent } from './components/sla/sla-datatable/sla-datatable.component';
import { FilterAddComponent } from './components/filter/filter-add/filter-add.component';
import { FilterEditComponent } from './components/filter/filter-edit/filter-edit.component';
import { FilterViewComponent } from './components/filter/filter-view/filter-view.component';
import { FilterDatatableComponent } from './components/filter/filter-datatable/filter-datatable.component';
import { CompanyAddComponent } from './components/company/company-add/company-add.component';
import { CompanyEditComponent } from './components/company/company-edit/company-edit.component';
import { CompanyViewComponent } from './components/company/company-view/company-view.component';
import { CompanyDatatableComponent } from './components/company/company-datatable/company-datatable.component';
import { AgentAddComponent } from './components/agent/agent-add/agent-add.component';
import { AgentEditComponent } from './components/agent/agent-edit/agent-edit.component';
import { AgentViewComponent } from './components/agent/agent-view/agent-view.component';
import { AgentDatatableComponent } from './components/agent/agent-datatable/agent-datatable.component';
import { HomeComponent } from './components/home/home.component';
import { AdminAddComponent } from './components/admin/admin-add/admin-add.component';
import { AdminEditComponent } from './components/admin/admin-edit/admin-edit.component';
import { AdminViewComponent } from './components/admin/admin-view/admin-view.component';
import { AdminDatatableComponent } from './components/admin/admin-datatable/admin-datatable.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ItemListComponent } from './components/item/item-list/item-list.component';

const routes: Routes = [
  { path: '', component: AdminDatatableComponent, pathMatch: 'full' },
  {
    path: 'admin',
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: AdminDatatableComponent },
      { path: 'view/:id', component: AdminViewComponent },
      { path: 'edit/:id', component: AdminEditComponent },
      { path: 'add', component: AdminAddComponent }
    ]
  },
  {
    path: 'agent',
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: AgentDatatableComponent },
      { path: 'view/:id', component: AgentViewComponent },
      { path: 'edit/:id', component: AgentEditComponent },
      { path: 'add', component: AgentAddComponent },
      { path: 'dashboard', component: AgentDashboardComponent }
    ]
  },
  {
    path: 'company',
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: CompanyDatatableComponent },
      { path: 'view/:id', component: CompanyViewComponent },
      { path: 'edit/:id', component: CompanyEditComponent },
      { path: 'add', component: CompanyAddComponent }
    ]
  },
  {
    path: 'filter',
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: FilterDatatableComponent },
      { path: 'view/:id', component: FilterViewComponent },
      { path: 'edit/:id', component: FilterEditComponent },
      { path: 'add', component: FilterAddComponent }
    ]
  },
  {
    path: 'sla',
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: SlaDatatableComponent },
      { path: 'view/:id', component: SlaViewComponent },
      { path: 'edit/:id', component: SlaEditComponent },
      { path: 'add', component: SlaAddComponent }
    ]
  },
  { path: 'home', component: HomeComponent },
  { path: 'extra', component: ExtraComponent },
  { path: 'login', component: LoginComponent },
  { path: '**', component: ErrorComponent }
  // { path: '', redirectTo: 'items/1', pathMatch: 'full' },
  // { path: 'items/:id', component: ItemListComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'ignore' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
