package az.baxtiyargil.graphqldemo.model.view;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BerryView {

    Long id;
    String name;
    String size;
    String growthTime;
    String maxHarvest;
    String naturalGiftPower;
    String soilDryness;

}
