import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { StartPageComponent } from './components/start-page/start-page.component';
import { MapPageComponent } from './components/map-page/map-page.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, StartPageComponent, MapPageComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'railway';
}
