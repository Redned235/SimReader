package me.redned.simreader.sc4.storage.exemplar.property.type;

import lombok.ToString;
import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;
import me.redned.simreader.sc4.type.occupant.OccupantGroupType;

import java.util.ArrayList;
import java.util.List;

@ToString
public class OccupantGroupTypesProperty extends ExemplarProperty.Multi<Integer> {
    private final List<OccupantGroupType> occupants = new ArrayList<>();

    public OccupantGroupTypesProperty(int size) {
        super(size);
    }

    @Override
    public void onReadComplete() {
        for (Integer id : this.values) {
            this.occupants.add(OccupantGroupType.byId(id));
        }
    }

    public List<OccupantGroupType> getOccupants() {
        return this.occupants;
    }
}
