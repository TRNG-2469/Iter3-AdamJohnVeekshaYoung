import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/AuthService';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-screen',
  imports: [RouterLink, FormsModule],
  templateUrl: './login-screen.html',
  styleUrl: './login-screen.css',
})
export class LoginScreen {
  username = '';
  password = '';
  errorMessage = '';
 
  constructor(private authService: AuthService, private router: Router) {}
 
  onSubmit(): void {
    this.errorMessage = '';
 
    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/reimbursements']);
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.errorMessage = 'Invalid username or password';
        } else {
          this.errorMessage = 'Something went wrong. Please try again.';
        }
      }
    });
  }
}
