import { Injectable } from '@angular/core';

export interface ReadingProgress {
  courseId: string;
  lessonId: string;
  position: number;
  updatedAt: number;
}

@Injectable({ providedIn: 'root' })
export class OfflineProgressService {
  private readonly databaseName = 'digilearn-offline';
  private readonly storeName = 'reading-progress';

  async save(progress: ReadingProgress): Promise<void> {
    const database = await this.openDatabase();
    await new Promise<void>((resolve, reject) => {
      const request = database.transaction(this.storeName, 'readwrite').objectStore(this.storeName).put(progress);
      request.onsuccess = () => resolve();
      request.onerror = () => reject(request.error);
    });
    database.close();
  }

  async get(courseId: string, lessonId: string): Promise<ReadingProgress | undefined> {
    const database = await this.openDatabase();
    const progress = await new Promise<ReadingProgress | undefined>((resolve, reject) => {
      const request = database.transaction(this.storeName).objectStore(this.storeName).get([courseId, lessonId]);
      request.onsuccess = () => resolve(request.result);
      request.onerror = () => reject(request.error);
    });
    database.close();
    return progress;
  }

  private openDatabase(): Promise<IDBDatabase> {
    return new Promise((resolve, reject) => {
      const request = indexedDB.open(this.databaseName, 1);
      request.onupgradeneeded = () => request.result.createObjectStore(this.storeName, { keyPath: ['courseId', 'lessonId'] });
      request.onsuccess = () => resolve(request.result);
      request.onerror = () => reject(request.error);
    });
  }
}
