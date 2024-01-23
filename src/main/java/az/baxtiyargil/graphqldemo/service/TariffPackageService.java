package az.baxtiyargil.graphqldemo.service;

import az.baxtiyargil.graphqldemo.client.PokemonBerryClient;
import az.baxtiyargil.graphqldemo.configuration.properties.QueryFilterProperties;
import az.baxtiyargil.graphqldemo.mapper.BerryMapper;
import az.baxtiyargil.graphqldemo.model.Filter;
import az.baxtiyargil.graphqldemo.model.view.BerryView;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TariffPackageService {

    private final EntityManager em;
    private final EntityViewManager evm;
    private final BerryMapper berryMapper;
    private final CriteriaBuilderFactory cbf;
    private final PokemonBerryClient berryClient;
    private final QueryFilterProperties queryFilterProperties;

    public BerryView findBerries(Integer id) {
        var result = berryClient.getBerryInfo(id);
        return berryMapper.mapToBerryView(result);
    }

    public <T> T findById(EntityViewSetting<T, CriteriaBuilder<T>> setting, String id) {
        return evm.find(em, setting, id);
    }

    public <T> List<T> findAll(EntityViewSetting<T, ?> setting) {
        return evm.applySetting(setting, cbf.create(em, evm.getMetamodel().managedView(
                        setting.getEntityViewClass()).getEntityClass()))
                .getResultList();
    }

    public <T> List<T> search(EntityViewSetting<T, CriteriaBuilder<T>> setting, Filter filter) throws IllegalAccessException {
        var criteriaBuilder = cbf.create(em, evm.getMetamodel()
                .managedView(setting.getEntityViewClass()).getEntityClass());
        var filterMap = filterAsMap(filter);
        filterMap.entrySet().stream()
                .filter(ent -> Objects.nonNull(ent.getValue()))
                .forEach((entry) -> {
                    var propEntry = findMatchingProp(entry.getKey());
                    appendRestriction(criteriaBuilder.where(propEntry.getKey()),
                            propEntry.getValue(), entry.getValue());
                });
        var query = evm.applySetting(setting, criteriaBuilder);
        return query.getResultList();
    }

    private void appendRestriction(RestrictionBuilder<?> restrictionBuilder, String restrictionType, Object value) {
        switch (restrictionType) {
            case "eq":
                restrictionBuilder.eq(value);
                break;
            case "like":
                restrictionBuilder.like(false).value(value);
                break;
            default:
                restrictionBuilder.eq(value);
        }
    }

    private Map<String, Object> filterAsMap(Filter filter) throws IllegalAccessException {
        Map<String, Object> fieldMap = new HashMap<>();
        Class<?> clazz = filter.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            fieldMap.put(field.getName(), field.get(filter));
        }

        return fieldMap;
    }

    private Map.Entry<String, String> findMatchingProp(String key) {
        return queryFilterProperties.getRestrictions().entrySet()
                .stream()
                .filter(filterKey -> filterKey.getKey().equals(key))
                .findAny()
                .get();
    }

}
