package microservice.product_service.mapper;

import lombok.RequiredArgsConstructor;
import microservice.common_service.exception.NotFoundException;
import microservice.common_service.model.PurchaseResponse;
import microservice.product_service.dto.product.ProductRequest;
import microservice.product_service.dto.product.ProductResponse;
import microservice.product_service.model.Category;
import microservice.product_service.model.Product;
import microservice.product_service.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepository categoryRepository;

    public Product toProduct(ProductRequest request) {
        return Product.builder()
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .category(findCategory(request.categoryId()))
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }

    public PurchaseResponse toPurchaseResponse(Product product, double quantity) {
        return new PurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }

    public Category findCategory(int id){
        return categoryRepository.findById(id).orElseThrow(()-> new NotFoundException("Category not found with ID:: " + id));
    }
}