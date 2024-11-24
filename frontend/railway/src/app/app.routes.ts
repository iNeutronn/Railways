import { Routes } from '@angular/router';
import { MapPageComponent } from './components/map-page/map-page.component';
import { StartPageComponent } from './components/start-page/start-page.component';

export const routes: Routes = [
    {path:'map', component: MapPageComponent},
    {path:'start', component: StartPageComponent},
    { path: '', redirectTo: '/map', pathMatch: 'full' }
];
