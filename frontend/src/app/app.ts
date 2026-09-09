import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TestServiceComponent } from './test-service-component/test-service-component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TestServiceComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
