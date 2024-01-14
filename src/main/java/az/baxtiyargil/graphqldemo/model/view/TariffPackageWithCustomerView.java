package az.baxtiyargil.graphqldemo.model.view;

import az.baxtiyargil.graphqldemo.model.entity.TariffPackage;
import com.blazebit.persistence.view.EntityView;

import java.util.Set;

@EntityView(TariffPackage.class)
public interface TariffPackageWithCustomerView extends TariffPackageView {

    Set<CustomerView> getCustomers();

}
