package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.*;
import org.ifpe.recicoin.entities.enums.TransactionType;
import org.ifpe.recicoin.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PointTransactionRepository transactionRepository;

    // 1. Mostrar todos os produtos para o usuario
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // 2. Empresa cadastra um novo produto
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof Company)) {
                return ResponseEntity.status(403).body("Apenas empresas parceiras podem cadastrar produtos.");
            }
            
            // Associa o produto à Empresa que está logada
            product.setCompany((Company) principal);
            org.ifpe.recicoin.repositories.ProductRepository productRepository = null; // Apenas para evitar erro no snippet, use o seu @Autowired
            productRepository.save(product);
            
            return ResponseEntity.ok("Produto cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    // 3. Usuário resgata um produto
    @PostMapping("/{productId}/redeem")
    public ResponseEntity<?> redeemProduct(@PathVariable Long productId) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Apenas usuários podem resgatar produtos.");
            }
            User user = (User) principal;
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

            // Consulta quantas moedas o usuário tem
            Integer saldoAtual = transactionRepository.getUserAmount(user.getId());
            if (saldoAtual == null) saldoAtual = 0;

            // Verifica se tem saldo suficiente
            if (saldoAtual < product.getPoints()) {
                return ResponseEntity.badRequest().body("Saldo insuficiente! Você tem " + saldoAtual + 
                        " ReciCoins, mas precisa de " + product.getPoints() + ".");
            }

            // Gera a transação de compra (desconta os pontos com valor negativo)
            PointTransaction transaction = new PointTransaction();
            transaction.setUser(user);
            transaction.setAmount(-product.getPoints()); // Tira os pontos!
            transaction.setType(TransactionType.PURCHASE_REDEEM);
            transaction.setReferenceId(product.getId());
            transaction.setCreatedAt(LocalDateTime.now());
            
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Resgate realizado! Você adquiriu: " + product.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao resgatar: " + e.getMessage());
        }
    }
}