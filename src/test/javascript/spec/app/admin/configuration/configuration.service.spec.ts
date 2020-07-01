import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ConfigurationService } from 'app/admin/configuration/configuration.service';
import { SERVER_API_URL } from 'app/app.constants';

describe('Service Tests', () => {
  describe('Logs Service', () => {
    let service: ConfigurationService;
    let httpMock: HttpTestingController;
    let expectedResult: any[] | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });

      expectedResult = null;
      service = TestBed.get(ConfigurationService);
      httpMock = TestBed.get(HttpTestingController);
    });

    afterEach(() => {
      httpMock.verify();
    });

    describe('Service methods', () => {
      it('should call correct URL', () => {
        service.get().subscribe(() => {});

        const req = httpMock.expectOne({ method: 'GET' });
        const resourceUrl = SERVER_API_URL + 'management/configprops';
        expect(req.request.url).toEqual(resourceUrl);
      });

      it('should get the config', () => {
        const angularConfig = {
          contexts: {
            angular: {
              beans: ['test2'],
            },
          },
        };
        service.get().subscribe((received: any) => {
          expectedResult = received;
        });

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(angularConfig);
        expect(expectedResult).toEqual(angularConfig); // We expect config to be a straight map
      });
      // We don't get property sources separately like Spring Boot, so no need for env test
    });
  });
});
