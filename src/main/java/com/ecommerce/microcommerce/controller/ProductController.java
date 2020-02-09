package com.ecommerce.microcommerce.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @GetMapping("/Produits")
    public List<Product> listeProduits() {
        log.info("Retourne la liste des produits");
        return productDao.findAll();
    }

    @GetMapping("/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        log.info("Affiche un produit");
        return productDao.findById(id);
    }

    @PostMapping("/Produits")
    public void ajouterProduit(@RequestBody Product product) {
        log.info("Sauvegarde un produit");
        productDao.save(product);
    }
}
