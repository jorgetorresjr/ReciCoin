package org.ifpe.recicoin.controllers;

import java.util.List;

import org.ifpe.recicoin.entities.Company;
import org.ifpe.recicoin.entities.Product;
import org.ifpe.recicoin.entities.DTOs.ProductDTO;
import org.ifpe.recicoin.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product create(@RequestBody ProductDTO dto,
                          @AuthenticationPrincipal Company company) {
        return productService.createProduct(dto, company);
    }

    @GetMapping
    public List<Product> list(@AuthenticationPrincipal Company company) {
        return productService.listCompanyProducts(company.getId());
    }
}