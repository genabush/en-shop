import { ContextSelectorPipe } from './context-selector.pipe';

describe('ContextSelectorPipe', () => {
  let pipe: ContextSelectorPipe;
  beforeAll(() => {
    pipe = new ContextSelectorPipe();
  });
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });
  it('should slice string', () => {
    const resp = pipe.transform({ nativeName: true, label: 'test' });

    expect(resp).toEqual('te');
  });
  it('should return label', () => {
    const resp = pipe.transform({ nativeName: false, label: 'test' });

    expect(resp).toEqual('test');
  });
});
