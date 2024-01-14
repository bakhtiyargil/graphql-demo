package az.baxtiyargil.graphqldemo.repository;

import az.baxtiyargil.graphqldemo.model.entity.TariffPackage;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TariffPackageRepository extends CrudRepository<TariffPackage, String>, QuerydslPredicateExecutor<TariffPackage> {
}
