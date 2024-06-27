package com.cosmomedia.podfex.service.product;

import com.cosmomedia.podfex.dto.OneResponse;
import com.cosmomedia.podfex.dto.ProductDto;
import com.cosmomedia.podfex.entities.Message;
import com.cosmomedia.podfex.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ProductCRUD {
    Page<ProductDto> getProductList(Pageable pageable);

    Page<ProductDto> getActiveProductList(Pageable pageable);

    OneResponse<ProductDto> getOneProduct(String name);



    Message addProduct(Product product);

    Message deleteProduct(Long id);

    @Transactional
    Message updateProduct(String name, Product updatedProduct);
}
