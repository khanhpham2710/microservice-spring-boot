package microservice.product_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.product_service.dto.product.ProductRequest;
import microservice.product_service.dto.product.ProductResponse;
import microservice.product_service.dto.purchase.PurchaseRequest;
import microservice.product_service.model.PurchaseResponse;
import microservice.product_service.model.ReturnRequest;
import microservice.product_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/purchase")
    public ResponseEntity<List<PurchaseResponse>> purchaseProducts(
            @RequestBody List<PurchaseRequest> request) {
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/return")
    public ResponseEntity<String> returnProducts(@RequestBody List<ReturnRequest> request) {
        productService.returnProducts(request);
        return ResponseEntity.ok("Return products successfully");
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable("product-id") Integer productId
    ) {
        return ResponseEntity.ok(productService.findById(productId));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Heloo");
    }
}
