import { Component, inject } from '@angular/core';
import { OfflineProgressService } from '../../core/services/offline-progress.service';

@Component({
  standalone: true,
  template: `<section class="page-heading"><p class="eyebrow">Mon apprentissage</p><h1>Continuez là où vous vous êtes arrêté.</h1><p>Les contenus consultés restent accessibles hors connexion.</p></section><section class="course-list"><article><div class="course-art">01</div><div><span class="tag">En cours · 64%</span><h2>Développement web essentiel</h2><p>Module 04 · Interfaces responsives</p><button (click)="saveProgress()">Reprendre le cours</button><small>{{ saved ? 'Progression enregistrée localement' : 'Prêt pour la synchronisation' }}</small></div></article><article><div class="course-art second">02</div><div><span class="tag">À démarrer</span><h2>Entreprendre au Burkina Faso</h2><p>8 modules · 3h 20 de contenu</p><button>Voir le parcours</button></div></article></section>`,
  styles: [`:host{display:block}.page-heading h1{font-family:'Space Grotesk',sans-serif;font-size:clamp(2rem,5vw,4rem);line-height:1;margin:.5rem 0;max-width:760px}.page-heading p:last-child{color:#63736c}.eyebrow{color:#0b6b50;font-size:.8rem;font-weight:700;letter-spacing:.14em;text-transform:uppercase}.course-list{display:grid;gap:1rem;margin-top:3rem}.course-list article{background:white;border:1px solid #dfe6df;display:grid;gap:1.5rem;grid-template-columns:180px 1fr;padding:1rem}.course-art{align-items:end;background:#0b6b50;color:#d6ed63;display:flex;font-family:'Space Grotesk',sans-serif;font-size:3rem;padding:1rem}.course-art.second{background:#de8b50;color:#fff}.tag{color:#0b6b50;font-size:.8rem;font-weight:700}.course-list h2{font-family:'Space Grotesk',sans-serif;margin:.7rem 0}.course-list p{color:#63736c}.course-list button{background:#0b6b50;border:0;border-radius:4px;color:white;padding:.7rem 1rem}.course-list small{color:#63736c;display:block;margin-top:.8rem}@media(max-width:600px){.course-list article{grid-template-columns:1fr}.course-art{min-height:100px}}
`]
})
export class ApprenantPageComponent {
  private readonly progress = inject(OfflineProgressService);
  saved = false;
  async saveProgress(): Promise<void> {
    await this.progress.save({ courseId: 'web-essentiel', lessonId: 'responsive', position: 0.64, updatedAt: Date.now() });
    this.saved = true;
  }
}
