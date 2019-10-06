import { AuthGuard } from './guards/auth.guard';
import { ExtraComponent } from './components/extra/extra.component';
import { ErrorComponent } from './components/error/error.component';
import { LoginComponent } from './components/login/login.component';
import { AgentDashboardComponent } from './components/agent/agent-dashboard/agent-dashboard.component';
import { SlaAddComponent } from './components/sla/sla-add/sla-add.component';
import { SlaEditComponent } from './components/sla/sla-edit/sla-edit.component';
import { SlaViewComponent } from './components/sla/sla-view/sla-view.component';
import { SlaDatatableComponent } from './components/sla/sla-datatable/sla-datatable.component';
import { SelectionAddComponent } from './components/selection/selection-add/selection-add.component';
import { SelectionEditComponent } from './components/selection/selection-edit/selection-edit.component';
import { SelectionViewComponent } from './components/selection/selection-view/selection-view.component';
import { SelectionDatatableComponent } from './components/selection/selection-datatable/selection-datatable.component';
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
import { ItemViewComponent } from './components/item/item-view/item-view.component';
import { FilterItemDatatableComponent } from './components/filter-item/filter-item-datatable/filter-item-datatable.component';
import { UserItemDatatableComponent } from './components/user-item/user-item-datatable/user-item-datatable.component';
import { NgxPermissionsModule, NgxPermissionsService, NgxPermissionsGuard } from 'ngx-permissions';
import { APP_BASE_HREF } from '@angular/common';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full', canActivate: [AuthGuard] },
  {
    path: 'admin',
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_SUPER_ADMIN'],
        redirectTo: ''
      }
    },
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: AdminDatatableComponent },
      { path: 'view/:id', component: AdminViewComponent },
      { path: 'edit/:id', component: AdminEditComponent },
      { path: 'add', component: AdminAddComponent }
    ]
  },
  {
    path: 'account/admin',
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_ADMIN'],
        redirectTo: ''
      }
    },
    children: [
      { path: '', redirectTo: 'view', pathMatch: 'full' },
      { path: 'view', component: AdminViewComponent },
      { path: 'edit', component: AdminEditComponent }
    ]
  },
  {
    path: 'agent/dashboard',
    component: AgentDashboardComponent,
    // canActivate: [AuthGuard, NgxPermissionsGuard],
    // data: {
    //   permissions: {
    //     only: ['ROLE_AGENT'],
    //     redirectTo: 'extra'
    //   }
    // }
  },
  {
    path: 'agent',
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_ADMIN', 'ROLE_SUPER_AGENT'],
        redirectTo: ''
      }
    },
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: AgentDatatableComponent },
      { path: 'view/:id', component: AgentViewComponent },
      { path: 'edit/:id', component: AgentEditComponent },
      { path: 'add', component: AgentAddComponent },
      // { path: 'dashboard', component: AgentDashboardComponent }
    ]
  },
  {
    path: 'account/agent',
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_AGENT'],
        redirectTo: ''
      }
    },
    children: [
      { path: '', redirectTo: 'view', pathMatch: 'full' },
      { path: 'view', component: AgentViewComponent },
      { path: 'edit', component: AgentEditComponent }
    ]
  },
  {
    path: 'company',
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_ADMIN'],
        redirectTo: ''
      }
    },
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
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_ADMIN'],
        redirectTo: ''
      }
    },
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
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_ADMIN'],
        redirectTo: ''
      }
    },
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: SlaDatatableComponent },
      { path: 'view/:id', component: SlaViewComponent },
      { path: 'edit/:id', component: SlaEditComponent },
      { path: 'add', component: SlaAddComponent }
    ]
  },
  {
    path: 'selection',
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_ADMIN', 'ROLE_AGENT'],
        redirectTo: ''
      }
    },
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: SelectionDatatableComponent },
      { path: 'view/:id', component: SelectionViewComponent },
      { path: 'edit/:id', component: SelectionEditComponent },
      { path: 'add', component: SelectionAddComponent }
    ]
  },
  {
    path: 'monitoring',
    canActivate: [AuthGuard, NgxPermissionsGuard],
    data: {
      permissions: {
        only: ['ROLE_ADMIN'],
        redirectTo: ''
      }
    },
    children: [
      { path: '', redirectTo: 'user-item/list', pathMatch: 'full' },
      { path: 'user-item', redirectTo: 'user-item/list', pathMatch: 'full' },
      { path: 'user-item/list', component: UserItemDatatableComponent },
      { path: 'filter-item', redirectTo: 'filter-item/list', pathMatch: 'full' },
      { path: 'filter-item/list', component: FilterItemDatatableComponent }
    ]
  },
  { path: 'item/:id', component: ItemViewComponent },
  { path: 'extra', component: ExtraComponent },
  { path: 'login', component: LoginComponent },
  { path: '**', component: ErrorComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes,
      {
        onSameUrlNavigation: 'ignore',
        enableTracing: true
      }),
    NgxPermissionsModule.forRoot(),
  ],
  providers: [{ provide: APP_BASE_HREF, useValue: '/web' }],
  exports: [RouterModule]
})
export class AppRoutingModule { }
