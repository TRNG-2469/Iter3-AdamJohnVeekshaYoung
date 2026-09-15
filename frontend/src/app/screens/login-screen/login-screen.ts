import { Component, inject } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/UserService';
import { AuthService } from '../../services/AuthService';

@Component({
  selector: 'app-login-screen',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login-screen.html',
  styleUrl: './login-screen.css',
})
export class LoginScreen {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private userService = inject(UserService);
  private authService = inject(AuthService)

  loginForm: FormGroup = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  loginError: string | null = null;

  get username() { return this.loginForm.get('username')!; }
  get password() { return this.loginForm.get('password')!; }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const {username, password} = this.loginForm.value;

    // Call the real JWT login endpoint
    this.authService.login(username, password).subscribe({
      next: (response) => {
        // Success: Store the JWT token returned by JwtResponse
        //localStorage.setItem('authToken', response.token);
        //localStorage.setItem('isAuthenticated', 'true');

        // Navigate to the reimbursements page
        this.router.navigate(['/reimbursements']);
      },
      error: (err) => {
        // Failure: Bad credentials (401)
        this.loginError = 'Invalid username or password.';
      }
    });
  }
}
