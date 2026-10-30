import { Injectable } from '@angular/core';
import { Observable, fromEvent, merge, map, startWith, distinctUntilChanged, shareReplay } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class NetworkStatusService {
  readonly isOnline$: Observable<boolean> = merge(
    fromEvent(window, 'online').pipe(map(() => true)),
    fromEvent(window, 'offline').pipe(map(() => false))
  ).pipe(startWith(navigator.onLine), distinctUntilChanged(), shareReplay({ bufferSize: 1, refCount: true }));
}
