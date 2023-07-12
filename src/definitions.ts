export interface ProximityPlugin {
  enable(): Promise<void>;
  disable(): Promise<void>;
}
