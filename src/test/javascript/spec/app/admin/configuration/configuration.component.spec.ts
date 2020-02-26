import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ConfigurationComponent } from 'app/admin/configuration/configuration.component';
import { ConfigurationService } from 'app/admin/configuration/configuration.service';

describe('Component Tests', () => {
  describe('ConfigurationComponent', () => {
    let comp: ConfigurationComponent;
    let fixture: ComponentFixture<ConfigurationComponent>;
    let service: ConfigurationService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ConfigurationComponent],
        providers: [ConfigurationService]
      })
        .overrideTemplate(ConfigurationComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ConfigurationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfigurationService);
    });

    describe('OnInit', () => {
      it('should set all default values correctly', () => {
        expect(comp.filter).toBe('');
      });
      it('Should call load all on init', () => {
        // GIVEN
        const body = [{ config: 'test', properties: 'test' }, { config: 'test2' }];
        spyOn(service, 'get').and.returnValue(of(body));

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.get).toHaveBeenCalled();
        expect(comp.allConfiguration).toEqual(body);
      });
    });
  });
});
