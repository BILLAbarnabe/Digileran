import { Routes } from '@angular/router';

export const routes: Routes = [
	{ path: '', loadComponent: () => import('./features/public/public-page.component').then((module) => module.PublicPageComponent) },
	{ path: 'auth', loadComponent: () => import('./features/auth/auth-page.component').then((module) => module.AuthPageComponent) },
	{ path: 'manager', loadComponent: () => import('./features/manager/manager-page.component').then((module) => module.ManagerPageComponent) },
	{ path: 'formateur', loadComponent: () => import('./features/formateur/formateur-page.component').then((module) => module.FormateurPageComponent) },
	{ path: 'apprenant', loadComponent: () => import('./features/apprenant/apprenant-page.component').then((module) => module.ApprenantPageComponent) },
	{ path: '**', redirectTo: '' }
];
