package az.baxtiyargil.graphqldemo.service;

import az.baxtiyargil.graphqldemo.client.PokemonBerryClient;
import az.baxtiyargil.graphqldemo.mapper.BerryMapper;
import az.baxtiyargil.graphqldemo.model.view.BerryView;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffPackageService {

    private final EntityManager em;
    private final EntityViewManager evm;
    private final BerryMapper berryMapper;
    private final CriteriaBuilderFactory cbf;
    private final PokemonBerryClient berryClient;

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

}
