import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink, RouterOutlet } from '@angular/router';
import { NetworkStatusService } from './core/services/network-status.service';

@Component({
  selector: 'app-root',
  imports: [RouterLink, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  readonly networkStatus = inject(NetworkStatusService);
  readonly isOnline = toSignal(this.networkStatus.isOnline$, { initialValue: true });
}
