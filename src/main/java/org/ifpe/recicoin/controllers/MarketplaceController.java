package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.PointTransaction;
import org.ifpe.recicoin.entities.Product;
import org.ifpe.recicoin.entities.ProductRedemption;
import org.ifpe.recicoin.entities.User;
import org.ifpe.recicoin.entities.enums.TransactionType;
import org.ifpe.recicoin.repositories.PointTransactionRepository;
import org.ifpe.recicoin.repositories.ProductRedemptionRepository;
import org.ifpe.recicoin.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/marketplace")
public class MarketplaceController {

    private final ProductRepository productRepository;
    private final PointTransactionRepository transactionRepository;
    private final ProductRedemptionRepository redemptionRepository;

    public MarketplaceController(ProductRepository productRepository, PointTransactionRepository transactionRepository, ProductRedemptionRepository redemptionRepository) {
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.redemptionRepository = redemptionRepository;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @PostMapping("/redeem/{productId}")
    public ResponseEntity<?> redeemProduct(@PathVariable Long productId) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof User)) return ResponseEntity.status(403).body("Apenas usuários podem resgatar produtos.");
            
            User user = (User) principal;
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

            Integer saldoAtual = transactionRepository.getUserAmount(user.getId());
            if (saldoAtual == null) saldoAtual = 0;

            if (saldoAtual < product.getPoints()) {
                return ResponseEntity.badRequest().body("Saldo insuficiente!");
            }

            PointTransaction transaction = new PointTransaction();
            transaction.setUser(user);
            transaction.setAmount(-product.getPoints());
            transaction.setType(TransactionType.PURCHASE_REDEEM);
            transaction.setReferenceId(product.getId());
            transaction.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            ProductRedemption redemption = new ProductRedemption();
            redemption.setUser(user);
            redemption.setProduct(product);
            redemption.setStatus("AGUARDANDO_RETIRADA");
            redemption.setCreatedAt(LocalDateTime.now());
            redemptionRepository.save(redemption);

            return ResponseEntity.ok("Resgate realizado! O item está aguardando retirada.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/my-redemptions")
    public ResponseEntity<?> getMyRedemptions() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) return ResponseEntity.status(403).build();
        
        List<ProductRedemption> list = redemptionRepository.findByUserIdOrderByCreatedAtDesc(((User) principal).getId());

        var dtoList = list.stream().map(r -> new Object() {
            public final Long id = r.getId();
            public final String productName = r.getProduct().getName();
            public final String companyName = r.getProduct().getCompany().getLegalName();
            public final String status = r.getStatus();
            public final String date = r.getCreatedAt().toString();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
}