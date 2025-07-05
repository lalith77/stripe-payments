package com.demo.store.controller;

import com.demo.store.DTOs.AddItemToCartRequest;
import com.demo.store.DTOs.CartDto;
import com.demo.store.DTOs.CartItemDto;
import com.demo.store.DTOs.UpdateCartItemRequest;
import com.demo.store.Mappers.CartMapper;
import com.demo.store.entities.Cart;
import com.demo.store.entities.CartItem;
import com.demo.store.entities.CartRepository;
import com.demo.store.repositories.ProductRepository;
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
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        var cart = new Cart();
        cartRepository.save(cart);
        CartDto cartDto = cartMapper.toDto(cart);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId,
                                                 @RequestBody AddItemToCartRequest addItemToCartRequest) {

        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(addItemToCartRequest.getProductId()).orElse(null);
        // if product is not valid
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        // if product already exists in cart, we need to increment quantity
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(1);
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);
        CartItemDto cartItemDto = cartMapper.toDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "cart not found")
            );
        }

        // check if the product exists as a cartItem in the cart
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "product not found in cart")
            );
        }

        cartItem.setQuantity(updateCartItemRequest.getQuantity());
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }
}
