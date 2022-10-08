package com.company.store.service;

import org.javatuples.Triplet;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;

public interface CartService {
    Triplet<String, Model, HttpStatus> addProductToCart(String productId, String quantity, Model model);

    @Transactional
    Pair<String, HttpStatus> proceedOrder();

    Triplet<String, RedirectAttributes, HttpStatus> editItemInCart(
            String productId,
            String newQuantity,
            RedirectAttributes redirectAttributes);
}
