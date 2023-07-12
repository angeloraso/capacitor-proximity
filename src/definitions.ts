export interface ProximityPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
