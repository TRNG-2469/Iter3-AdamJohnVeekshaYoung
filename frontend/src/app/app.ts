import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginScreen } from './login-screen/login-screen';
import { RegisterScreen } from './register-screen/register-screen';
import { ReimbursementsScreen } from './reimbursements-screen/reimbursements-screen';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,LoginScreen,RegisterScreen,ReimbursementsScreen],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
