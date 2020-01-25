import { CustomHighlightPipe } from './custom-highlight.pipe';

describe('CustomHighlightPipe', () => {
  it('create an instance', () => {
    const pipe = new CustomHighlightPipe();
    expect(pipe).toBeTruthy();
  });
  it('should transform', () => {
    const pipe = new CustomHighlightPipe();
    const res = pipe.transform('test', 'test1');

    expect(res).toEqual('test');
  });
});
