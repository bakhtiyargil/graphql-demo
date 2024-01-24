package az.baxtiyargil.graphqldemo.model.enumeration;

import com.blazebit.persistence.RestrictionBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static az.baxtiyargil.graphqldemo.model.constant.ApplicationConstants.PCT;

@Getter
@AllArgsConstructor
public enum RestrictionType {

    EQUALS(RestrictionBuilder::eq),
    GREATER_THAN(RestrictionBuilder::gt),
    LIKE((rb, value) -> rb.like(false).value(value + PCT).noEscape());

    private final BiConsumer<RestrictionBuilder<?>, Object> method;
    public static final Map<RestrictionType, BiConsumer<RestrictionBuilder<?>, Object>> RESTRICTIONS;

    static {
        RESTRICTIONS = Arrays.stream(values())
                .collect(Collectors.toMap(restrictionType -> restrictionType, RestrictionType::getMethod));
    }

}
