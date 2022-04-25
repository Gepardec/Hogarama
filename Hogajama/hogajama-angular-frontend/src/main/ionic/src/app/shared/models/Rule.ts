export interface Rule {
    id?: number;
    name?: string;
    description?: string;
    sensorId?: number;
    actorId?: number;
    unitId?: number;
    waterDuration?: number;
    lowWater?: number;
}
