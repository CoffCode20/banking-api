package co.istad.spring_boot_2.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String actName;

    private String actNo;

    private BigDecimal balance;

    private BigDecimal overLimit;

    private Boolean isDeleted;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cust_id")
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "acc_type_id")
    private AccountType accountType;

    @OneToMany(mappedBy = "sender")
    private List<Transaction> transactions;

}
