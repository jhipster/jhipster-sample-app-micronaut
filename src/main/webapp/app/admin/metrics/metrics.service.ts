import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

export type MetricsKey = 'jvm' | 'http.server.requests' | 'cache' | 'services' | 'databases' | 'garbageCollector' | 'processMetrics';
export type Metrics = { [key in MetricsKey]: any };
export type Thread = {
  threadName?: string;
  threadId?: number;
  threadState?: string;
  suspended?: boolean;
  stackTrace?: StackElement[];
  // ... just trying to give an idea of what should be expected w/o making testing onerous
};
export type StackElement = {
  methodName?: string;
  fileName?: string;
  lineNumber?: number;
  // ... just trying to give an idea of what should be expected w/o making testing onerous
};

@Injectable({ providedIn: 'root' })
export class MetricsService {
  constructor(private http: HttpClient) {}

  getMetrics(): Observable<Metrics> {
    return this.http.get<Metrics>(SERVER_API_URL + 'management/jhimetrics');
  }

  threadDump(): Observable<Thread[]> {
    return this.http.get<Thread[]>(SERVER_API_URL + 'management/threaddump');
  }
}
