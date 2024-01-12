package az.baxtiyargil.graphqldemo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
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

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_customer", schema = "graphql")
public class StudentCustomer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String universityName;

    @Column(name = "student_uuid")
    private String studentUUID;

    private LocalDate createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @ToString.Exclude
    private Customer customer;

}
