package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.PointTransaction;
import org.ifpe.recicoin.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/statement/{userId}")
        public List<PointTransaction> getStatement(@PathVariable Long userId) {
            return pointService.getStatement(userId);
        }
    @GetMapping("/my-balance")
    public ResponseEntity<Integer> getMyBalance() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.ifpe.recicoin.entities.User) {
            org.ifpe.recicoin.entities.User user = (org.ifpe.recicoin.entities.User) principal;
            return ResponseEntity.ok(pointService.getBalance(user.getId()));
        }
        return ResponseEntity.status(403).build();
    }

}
