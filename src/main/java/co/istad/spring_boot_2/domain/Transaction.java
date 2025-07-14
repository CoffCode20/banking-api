package co.istad.spring_boot_2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private TransactionType transactionType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    @Column(nullable = false)
    private BigDecimal amount;

    private String remark;

}
