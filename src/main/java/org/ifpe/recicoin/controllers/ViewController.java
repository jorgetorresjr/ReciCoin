package org.ifpe.recicoin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // [NOVO] Quando acessar localhost:8080, manda para o Login
    @GetMapping("/")
    public String root() {
        return "redirect:/login"; 
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String registerUserPage() {
        return "cadastroUser";
    }

    @GetMapping("/cadastro-ponto")
    public String registerPointPage() {
        return "cadastroPonto";
    }
    
    @GetMapping("/pontos-coleta")
    public String listPointsPage() {
        return "listaPontos";
    }

    @GetMapping("/meu-painel")
    public String dashboardPage() {
        return "dashboardPonto";
    }
}