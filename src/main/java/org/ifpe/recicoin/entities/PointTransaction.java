package org.ifpe.recicoin.entities;

import jakarta.persistence.*;
import org.ifpe.recicoin.entities.enums.TransactionType;

import java.time.LocalDateTime;

@Entity
public class PointTransaction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Long referenceId;

    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
