package com.cosmomedia.location.service.product;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.dto.ProductDto;
import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Product;
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
