package org.ifpe.recicoin.service;

import org.ifpe.recicoin.entities.enums.MaterialType;
import org.ifpe.recicoin.entities.PointTransaction;
import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.entities.enums.TransactionType;
import org.ifpe.recicoin.repositories.PointTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PointService {

    @Autowired
    PointTransactionRepository repo;

    public void rewardDelivery(User user, Long referenceId, MaterialType type, double weight) {

        int points = calculatePoints(type, weight);

        PointTransaction transaction = new PointTransaction();
        transaction.setUser(user);
        transaction.setAmount(points);
        transaction.setType(TransactionType.DELIVERY_REWARD);
        transaction.setReferenceId(referenceId);
        transaction.setCreatedAt(LocalDateTime.now());

        repo.save(transaction);
    }

    private int calculatePoints(MaterialType type, double kg) {
        int rate = switch(type) {
            case PLASTIC -> 10;
            case METAL -> 15;
            case GLASS -> 8;
            case PAPER -> 5;
            case ELECTRONIC -> 25;
        };

        return (int) Math.round(rate * kg);
    }

    public Integer getBalance(Long userId) {
        return repo.getUserAmount(userId);
    }

    public List<PointTransaction> getStatement(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}

