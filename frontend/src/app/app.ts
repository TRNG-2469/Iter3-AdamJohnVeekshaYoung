import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginScreen } from './login-screen/login-screen';
import { RegisterScreen } from './register-screen/register-screen';
import { ReimbursementsScreen } from './reimbursements-screen/reimbursements-screen';
import { TestServiceComponent } from './test-service-component/test-service-component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,LoginScreen,RegisterScreen,ReimbursementsScreen,TestServiceComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
