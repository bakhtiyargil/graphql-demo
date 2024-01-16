package az.baxtiyargil.graphqldemo.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BerryResponse {

    Long id;
    String name;
    String size;
    @JsonProperty("growth_time")
    String growthTime;
    @JsonProperty("max_harvest")
    String maxHarvest;
    @JsonProperty("natural_gift_power")
    String naturalGiftPower;
    @JsonProperty("soil_dryness")
    String soilDryness;

}
