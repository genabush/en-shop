import { createEntityAdapter, EntityAdapter, EntityState } from '@ngrx/entity';
import { ICartVoucherState } from 'src/app/interfaces/custom-cart.interface';
import { Voucher } from '@spartacus/core';

export const featureAdapter: EntityAdapter<ICartVoucherState> = createEntityAdapter<ICartVoucherState>();

export interface State extends EntityState<ICartVoucherState> {
  appliedVouchers?: Voucher[];
}

export const initialState: State = featureAdapter.getInitialState({
  appliedVouchers: []
});
