package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.PointTransaction;
import org.ifpe.recicoin.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/points")
public class PointController {
    @Autowired
    private PointService pointService;

    @GetMapping("/balance/{userId}")
    public Integer getBalance(@PathVariable Long userId) {
        return pointService.getBalance(userId);
    }

    @GetMapping("/statement/{userId")
        public List<PointTransaction> getStatement(@PathVariable Long userId) {
            return pointService.getStatement(userId);
        }

}
