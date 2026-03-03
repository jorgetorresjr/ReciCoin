package org.ifpe.recicoin.service;

import java.util.List;

import org.ifpe.recicoin.entities.Company;
import org.ifpe.recicoin.entities.Product;
import org.ifpe.recicoin.entities.DTOs.ProductDTO;
import org.ifpe.recicoin.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductDTO dto, Company company) {

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPoints(dto.points());
        product.setCompany(company);

        return productRepository.save(product);
    }

    public List<Product> listCompanyProducts(Long companyId) {
        return productRepository.findByCompanyId(companyId);
    }
}