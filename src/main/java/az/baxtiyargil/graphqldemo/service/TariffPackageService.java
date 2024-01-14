package az.baxtiyargil.graphqldemo.service;

import az.baxtiyargil.graphqldemo.model.entity.TariffPackage;
import az.baxtiyargil.graphqldemo.repository.TariffPackageRepository;
import graphql.schema.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TariffPackageService {

    private final TariffPackageRepository tariffPackageRepository;

    public DataFetcher<TariffPackage> findTariffPackageById() {
        return QuerydslDataFetcher.builder(tariffPackageRepository)
                .projectAs(TariffPackage.class)
                .single();
    }

    public DataFetcher<Iterable<TariffPackage>> findAllTariffPackages() {
        return QuerydslDataFetcher.builder(tariffPackageRepository)
                .projectAs(TariffPackage.class)
                .many();
    }

}
