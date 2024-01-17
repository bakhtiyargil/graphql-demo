package az.baxtiyargil.graphqldemo.mapper;


import az.baxtiyargil.graphqldemo.client.BerryResponse;
import az.baxtiyargil.graphqldemo.model.view.BerryView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BerryMapper {

    BerryView mapToBerryView(BerryResponse berry);

}
