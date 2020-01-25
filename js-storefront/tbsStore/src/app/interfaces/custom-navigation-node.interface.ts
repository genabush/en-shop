export interface NavigationNode {
  title?: string;
  icon?: string;
  /** The url or route (parts) */
  url?: string | string[];

  target?: string | boolean;

  children?: Array<NavigationNode>;
  amplienceId?: string;
  enabledForMegaNav?: string;
  enabledForMobileNav?: string;
}
