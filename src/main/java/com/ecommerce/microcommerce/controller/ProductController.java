package com.ecommerce.microcommerce.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exceptions.ProduitIntrouvableException;
import com.ecommerce.microcommerce.model.Product;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @GetMapping("/Produits")
    public MappingJacksonValue listeProduits() {
        log.info("Retourne la liste des produits");
        List<Product>            produits          = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre         = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider           listedeNosFiltres = new SimpleFilterProvider().addFilter("filtreDynamique", monFiltre);
        MappingJacksonValue      produitsFiltre    = new MappingJacksonValue(produits);
        produitsFiltre.setFilters(listedeNosFiltres);
        return produitsFiltre;
    }

    @GetMapping("/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        log.info("Affiche un produit");
        Product product = productDao.findById(id);
        if (product == null)
            throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est introuvable. Ecran bleu si je pouvais");
        return product;
    }

    @GetMapping("/test/produits/{prixLimit}")
    public List<Product> testDeRequete(@PathVariable int prixLimit) {
        return productDao.findByPrixGreaterThan(prixLimit);
    }

    @PostMapping("/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {
        log.info("Sauvegarde un produit");
        Product productAdded = productDao.save(product);
        if (productAdded == null)
            return ResponseEntity.noContent().build();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
        productDao.delete(productDao.findById(id));
    }

    @PutMapping("/Produits/{id}")
    public void updateProduit(@RequestBody Product product) {
        productDao.save(product);
    }
}
