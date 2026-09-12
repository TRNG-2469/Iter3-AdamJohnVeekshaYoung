import { Routes } from '@angular/router';
import { LoginScreen } from './screens/login-screen/login-screen';
import { RegisterScreen } from './screens/register-screen/register-screen';
import { ReimbursementsScreen } from './screens/reimbursements-screen/reimbursements-screen';

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
