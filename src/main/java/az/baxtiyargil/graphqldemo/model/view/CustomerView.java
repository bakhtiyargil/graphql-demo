package az.baxtiyargil.graphqldemo.model.view;

import az.baxtiyargil.graphqldemo.model.entity.Customer;
import com.blazebit.persistence.view.EntityView;

@EntityView(Customer.class)
public interface CustomerView {

    Long getId();

    String getFullName();

    String getPhoneNumber();

    String getAddress();

}
