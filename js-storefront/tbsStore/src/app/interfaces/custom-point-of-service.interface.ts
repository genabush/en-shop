import { PointOfService } from '@spartacus/core/src/model/point-of-service.model';

export interface CustomPointOfService extends PointOfService {
  imageUrl?: string;
}
