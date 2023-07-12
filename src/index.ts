import { registerPlugin } from '@capacitor/core';

import type { ProximityPlugin } from './definitions';

const Proximity = registerPlugin<ProximityPlugin>('Proximity', {
  web: () => import('./web').then(m => new m.ProximityWeb()),
});

export * from './definitions';
export { Proximity };
