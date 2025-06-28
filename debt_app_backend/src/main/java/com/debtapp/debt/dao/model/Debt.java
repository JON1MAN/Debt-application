package com.debtapp.debt.dao.model;

import com.debtapp.user.dao.model.AbstractEntity;
import com.debtapp.user.dao.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Debt extends AbstractEntity {

    private String title;
    private String receiver;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User debtorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User creditorUser;
}

