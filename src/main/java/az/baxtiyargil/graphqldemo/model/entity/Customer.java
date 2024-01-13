package az.baxtiyargil.graphqldemo.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer", schema = "graphql")
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fullName;

    private String phoneNumber;

    private String address;

    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_package_id")
    @ToString.Exclude
    private TariffPackage tariffPackage;

    @OneToOne(mappedBy = "customer", cascade = {CascadeType.ALL})
    private StudentCustomer studentCustomer;

    @OneToOne(mappedBy = "customer", cascade = {CascadeType.ALL})
    private DoctorCustomer doctorCustomer;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private Set<CustomerPropertyValue> customerPropertyValues = new HashSet<>();

}
