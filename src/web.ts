import { WebPlugin } from '@capacitor/core';

import type { ProximityPlugin } from './definitions';

export class ProximityWeb extends WebPlugin implements ProximityPlugin {
  async enable(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  } 

  async disable(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  } 
}
