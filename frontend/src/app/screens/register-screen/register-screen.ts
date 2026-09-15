import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register-screen',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register-screen.html',
  styleUrl: './register-screen.css'
})
export class RegisterScreen implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);

  // Form fields matching your backend expectations
  formData = {
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    departmentId: null
  };

  departments: any[] = [];
  errorMessage: string = '';

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8081/api/departments').subscribe({
      next: (data) => {
        console.log('Departments response from backend:', data); // Check F12 console to see the exact keys!
        this.departments = data;
      },
      error: (err) => {
        console.error('Failed to load departments', err);
      }
    });
  }

  onRegister(): void {
    this.http.post('http://localhost:8081/api/users', this.formData).subscribe({
      next: () => {
        // Successfully registered, navigate back to the login page
        this.router.navigate(['/']); // Adjust route if your login page is at a specific path like '/login'
      },
      error: (err) => {
        console.error('Registration failed', err);
        this.errorMessage = 'Registration failed. Please check your inputs and try again.';
      }
    });
  }
}
