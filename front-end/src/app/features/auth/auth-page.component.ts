import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `<section class="auth-layout"><div class="auth-copy"><p class="eyebrow">Espace sécurisé</p><h1>Votre parcours,<br>à portée de main.</h1><p>Connectez-vous pour retrouver vos cours et votre progression, même avec une connexion intermittente.</p></div><form class="auth-form" (submit)="$event.preventDefault()"><label>Adresse e-mail<input type="email" name="email" required placeholder="vous@exemple.bf"></label><label>Mot de passe<input type="password" name="password" required placeholder="••••••••"></label><button class="primary" type="submit">Se connecter</button><p class="hint">Activation par token disponible pendant 72 heures.</p></form></section>`,
  styles: [`:host{display:block}.auth-layout{display:grid;gap:clamp(2rem,8vw,8rem);grid-template-columns:1.2fr .8fr;max-width:1000px;margin:5vh auto}.auth-copy{background:#d6ed63;padding:clamp(2rem,5vw,5rem)}.auth-copy h1{font-family:'Space Grotesk',sans-serif;font-size:clamp(2.5rem,5vw,5rem);line-height:.95;margin:2rem 0 1rem}.auth-copy p:last-child{line-height:1.6}.eyebrow{color:#0b6b50;font-size:.8rem;font-weight:700;letter-spacing:.14em;text-transform:uppercase}.auth-form{align-self:center;display:grid;gap:1rem}.auth-form label{display:grid;color:#63736c;font-size:.85rem;gap:.5rem}.auth-form input{border:1px solid #dfe6df;border-radius:4px;padding:.9rem}.primary{background:#0b6b50;border:0;border-radius:4px;color:white;cursor:pointer;padding:.9rem}.hint{color:#63736c;font-size:.8rem}@media(max-width:700px){.auth-layout{grid-template-columns:1fr;margin:1rem auto}}`]
})
export class AuthPageComponent {}
