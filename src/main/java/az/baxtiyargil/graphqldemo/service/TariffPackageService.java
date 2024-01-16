package az.baxtiyargil.graphqldemo.service;

import az.baxtiyargil.graphqldemo.model.entity.TariffPackage;
import az.baxtiyargil.graphqldemo.repository.TariffPackageRepository;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import graphql.schema.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffPackageService {

    private final EntityManager em;
    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final TariffPackageRepository tariffPackageRepository;

    public DataFetcher<TariffPackage> findTariffPackagesWithBerry(Integer id) {
        return QuerydslDataFetcher.builder(tariffPackageRepository)
                .projectAs(TariffPackage.class)
                .single();
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
