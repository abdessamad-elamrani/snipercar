import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ItemListComponent } from './components/item/item-list/item-list.component';

const routes: Routes = [
  { path: '', redirectTo: 'items/1', pathMatch: 'full' },
  { path: 'items/:id', component: ItemListComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'ignore' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
