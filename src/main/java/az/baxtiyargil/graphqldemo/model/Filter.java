package az.baxtiyargil.graphqldemo.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Filter {

    String id;
    String packageType;
    BigDecimal commission;

}
