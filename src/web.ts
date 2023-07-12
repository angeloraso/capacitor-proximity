import { WebPlugin } from '@capacitor/core';

import type { ProximityPlugin } from './definitions';

export class ProximityWeb extends WebPlugin implements ProximityPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
