import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';

export type HealthStatus = 'UP' | 'DOWN' | 'UNKNOWN' | 'OUT_OF_SERVICE';

export type HealthKey = 'diskSpace' | 'mail' | 'ping' | 'jdbc' | 'db';

export interface Health {
  name?: string;
  status: HealthStatus;
  details: {
    [key in HealthKey]?: HealthDetails;
  };
}

export interface HealthDetails {
  status: HealthStatus;
  details: any;
}

@Injectable({ providedIn: 'root' })
export class HealthService {
  constructor(private http: HttpClient) {}

  checkHealth(): Observable<Health> {
    const nameTranslationMap = {
      jdbc: 'db',
    };

    return this.http.get<Health>(SERVER_API_URL + 'management/health').pipe(
      map((response: Health) => {
        const mappedDetails = {};

        Object.keys(response.details).forEach(key => {
          const mappedKey = nameTranslationMap[key] ? nameTranslationMap[key] : key;
          mappedDetails[mappedKey] = response.details[key];
        });

        response.details = mappedDetails;

        return response;
      })
    );
  }
}
