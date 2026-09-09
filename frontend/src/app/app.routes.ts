import { Routes } from '@angular/router';
import { LoginScreen } from './login-screen/login-screen';
import { RegisterScreen } from './register-screen/register-screen';
import { ReimbursementsScreen } from './reimbursements-screen/reimbursements-screen';

export const routes: Routes = [
    {
        path: '',
        component: LoginScreen,
    },
    {
        path: 'register',
        component: RegisterScreen,
    },
    {
        path: 'reimbursements',
        component: ReimbursementsScreen,
    }
];
