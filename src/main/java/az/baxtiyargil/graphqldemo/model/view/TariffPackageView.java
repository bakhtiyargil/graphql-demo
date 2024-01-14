package az.baxtiyargil.graphqldemo.model.view;

import az.baxtiyargil.graphqldemo.model.entity.TariffPackage;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import java.math.BigDecimal;

@EntityView(TariffPackage.class)
public interface TariffPackageView {

    @IdMapping
    String getId();

    String getPackageType();

    BigDecimal getCommission();

}