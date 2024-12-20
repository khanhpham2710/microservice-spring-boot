package microservice.product_service.service.impl;

import lombok.RequiredArgsConstructor;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.NotFoundException;
import microservice.common_service.model.PurchaseResponse;
import microservice.product_service.dto.product.ProductRequest;
import microservice.product_service.dto.product.ProductResponse;
import microservice.product_service.dto.purchase.PurchaseRequest;
import microservice.product_service.exception.PurchaseException;
import microservice.product_service.mapper.ProductMapper;
import microservice.product_service.model.Product;
import microservice.product_service.repository.ProductRepository;
import microservice.product_service.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(
            ProductRequest request
    ) {
        Product product = productMapper.toProduct(request);
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse findById(Integer id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new NotFoundException("Product not found with ID:: " + id));
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = AppException.class)
    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> request) {
        List<Integer> productIds = request
                .stream()
                .map(PurchaseRequest::productId)
                .toList();
        List<Product> storedProducts = productRepository.findAllByIdInOrderById(productIds);
        List<PurchaseRequest> sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(PurchaseRequest::productId))
                .toList();

        List<PurchaseResponse> purchasedProducts = new ArrayList<>();
        for (int i = 0; i < storedProducts.size(); i++) {
            Product product = storedProducts.get(i);
            PurchaseRequest purchaseRequest = sortedRequest.get(i);
            if (product.getAvailableQuantity() < purchaseRequest.quantity()) {
                throw new PurchaseException("Insufficient stock quantity for product with ID:: " + purchaseRequest.productId());
            }
            double newAvailableQuantity = product.getAvailableQuantity() - purchaseRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);
            purchasedProducts.add(productMapper.toPurchaseResponse(product, purchaseRequest.quantity()));
        }
        return purchasedProducts;
    }
}
