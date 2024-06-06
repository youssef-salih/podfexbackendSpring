package com.cosmomedia.location.service.product;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.dto.ProductDto;
import com.cosmomedia.location.entities.Product;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.repositories.ProductRepository;

import com.cosmomedia.location.service.converters.Converters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCrudImpl implements ProductCRUD {

    private final ProductRepository productRepository;
    private final Converters converters;

    @Override
    public Page<ProductDto> getProductList(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return new PageImpl<>(
                productPage.stream()
                        .map(converters::convertToProductDto)
                        .toList(),
                pageable,
                productPage.getTotalElements()
        );

    }

    @Override
    public OneResponse<ProductDto> getOneProduct(String name) {
        Optional<Product> productOptional = productRepository.findByName(name);
        if (productOptional.isPresent()) {
            ProductDto productDto = converters.convertToProductDto(productOptional.get());
            return OneResponse.<ProductDto>builder()
                    .success(true)
                    .content(productDto)
                    .message("Product found")
                    .build();
        } else {
            return OneResponse.<ProductDto>builder()
                    .success(false)
                    .content(null)
                    .message("Product not found")
                    .build();
        }

    }
}
