package com.cosmomedia.location.service.product;

import com.cosmomedia.location.entities.Product;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCrudImpl implements ProductCRUD{

    private final ProductRepository productRepository;
    @Override
    public Page<Product> getProductList(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
