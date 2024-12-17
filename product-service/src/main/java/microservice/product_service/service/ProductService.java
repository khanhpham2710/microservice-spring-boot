package microservice.product_service.service;


import microservice.product_service.dto.product.ProductRequest;
import microservice.product_service.dto.product.ProductResponse;
import microservice.product_service.dto.purchase.PurchaseRequest;
import microservice.product_service.dto.purchase.PurchaseResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse findById(Integer id);
    List<ProductResponse> findAll();
    List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> request);
}
