import {Unit} from './Unit';
import {Sensor} from './Sensor';
import {Actor} from './Actor';

export interface UnitWithSensorsAndActors extends Unit{
    sensors?: Sensor[];
    actors?: Actor[];
}

export const getCompleteUnits = (units: Unit[], sensors: Sensor[], actors: Actor[]): UnitWithSensorsAndActors[] => {
    if (units == null) return [];

    return units
        .map(u => ({
            ...u,
            sensors: sensors.filter(s => s.unitId == u.id),
            actors: actors.filter(a => a.unitId == u.id)
        } as UnitWithSensorsAndActors))
        // Sort by isDefault, so that the default unit is always first in the result array
        .sort((u1, u2) => (u2.defaultUnit?1:0) - (u1.defaultUnit?1:0));
}
