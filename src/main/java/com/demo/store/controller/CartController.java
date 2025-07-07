package com.demo.store.controller;

import com.demo.store.DTOs.AddItemToCartRequest;
import com.demo.store.DTOs.CartDto;
import com.demo.store.DTOs.CartItemDto;
import com.demo.store.DTOs.UpdateCartItemRequest;
import com.demo.store.exceptions.CartNotFoundException;
import com.demo.store.exceptions.ProductNotFoundException;
import com.demo.store.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        CartDto cartDto = cartService.createCart();
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "adds a product to the cart")
    public ResponseEntity<CartItemDto> addToCart(@Parameter(description = "The Id of the cart")
                                                 @PathVariable UUID cartId,
                                                 @RequestBody AddItemToCartRequest addItemToCartRequest) {

        CartItemDto cartItemDto = cartService.addItemToCart(cartId, addItemToCartRequest.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        CartItemDto cartItemDto = cartService.updateCartItem(cartId, productId, updateCartItemRequest.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Map<String, String>> deleteCartItem(@PathVariable Long productId, @PathVariable UUID cartId) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Map<String, String>> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "cart not found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "product not found in the cart")
        );
    }


}
