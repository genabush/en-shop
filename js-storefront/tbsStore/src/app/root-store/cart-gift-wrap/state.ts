import { createEntityAdapter, EntityAdapter, EntityState } from '@ngrx/entity';
import { IGiftWrapMessage } from 'src/app/interfaces/custom-cart.interface';

export const featureAdapter: EntityAdapter<IGiftWrapMessage> = createEntityAdapter<IGiftWrapMessage>();

export interface State extends EntityState<IGiftWrapMessage> {
  giftMessage?: string;
  giftMessageName?: string;
  giftMessageSenderName?: string;
}

export const initialState: State = featureAdapter.getInitialState({
  giftMessage: undefined,
  giftMessageName: undefined,
  giftMessageSenderName: undefined,
  giftWrapApplied: false,
  giftWrapServiceImage: undefined,
  giftWrapServiceMessage: undefined
});
