import { PayNowEnabledPipe } from './pay-now-enabled.pipe';

describe('PayNowEnabledPipe', () => {
  it('create an instance', () => {
    const pipe = new PayNowEnabledPipe();
    expect(pipe).toBeTruthy();
  });
});
