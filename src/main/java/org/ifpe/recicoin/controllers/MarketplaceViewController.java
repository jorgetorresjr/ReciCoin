package org.ifpe.recicoin.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MarketplaceViewController {
    @GetMapping("/cadastro-empresa")
    public String cadastroEmpresa() { return "cadastroEmpresa"; }
}