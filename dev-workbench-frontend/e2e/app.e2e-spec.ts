import { DevWorkbenchFrontendPage } from './app.po';

describe('dev-workbench-frontend App', () => {
  let page: DevWorkbenchFrontendPage;

  beforeEach(() => {
    page = new DevWorkbenchFrontendPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
