package az.baxtiyargil.graphqldemo.controller;

import az.baxtiyargil.graphqldemo.model.Filter;
import az.baxtiyargil.graphqldemo.service.TariffPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class TariffPackageController {

    private final TariffPackageService tariffPackageService;

    public void search(@Argument Filter filter) {
        //
        System.out.println("");
    }

}
