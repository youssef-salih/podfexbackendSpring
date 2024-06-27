package com.cosmomedia.podfex.service.product;

import com.cosmomedia.podfex.dto.OneResponse;
import com.cosmomedia.podfex.dto.ProductDto;
import com.cosmomedia.podfex.entities.Message;
import com.cosmomedia.podfex.entities.Product;
import com.cosmomedia.podfex.entities.Surface;
import com.cosmomedia.podfex.repositories.ProductRepository;

import com.cosmomedia.podfex.repositories.SurfaceRepository;
import com.cosmomedia.podfex.service.converters.Converters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCrudImpl implements ProductCRUD {

    private final ProductRepository productRepository;
    private final Converters converters;
    private final SurfaceRepository surfaceRepository;

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
    public Page<ProductDto> getActiveProductList(Pageable pageable) {
        Page<Product> productPage = productRepository.findByActiveTrue(pageable);
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

    private static byte[] convertStringToBytes(String byteString) {
        // Remove square brackets from the input string
        byteString = byteString.replaceAll("\\[|\\]", "");

        String[] byteValues = byteString.split(",");
        byte[] byteArray = new byte[byteValues.length];

        for (int i = 0; i < byteValues.length; i++) {
            byteArray[i] = Byte.parseByte(byteValues[i].trim());
        }

        return byteArray;
    }

    @Override
    @Transactional
    public Message addProduct(Product product) {
        // Convert base64 strings to byte arrays
        byte[] imageBytes = Base64.getDecoder().decode(product.getImage());

        byte[] frontSurfaceBytes = Base64.getDecoder().decode(product.getSurface().getFront());
        byte[] backSurfaceBytes = Base64.getDecoder().decode(product.getSurface().getBack());

        // Create Surface entity
        Surface surface = Surface.builder()
                .front(frontSurfaceBytes)
                .back(backSurfaceBytes)
                .build();
        surfaceRepository.save(surface);

        // Create Product entity
        Product productInsert = Product.builder()
                .name(product.getName())
                .price(product.getPrice())
                .image(imageBytes)
                .surface(surface)
                .sizes(product.getSizes())
                .colors(product.getColors())
                .active(product.getActive())
                .build();
        productRepository.save(productInsert);
        return Message.builder()
                .success(true)
                .message("Product added successfully")
                .build();
    }
    @Override
    public Message deleteProduct(Long id) {
        try {
            Optional<Product> existingProductOptional = productRepository.findById(id);
            if (existingProductOptional.isPresent()) {
                productRepository.delete(existingProductOptional.get());
                return new Message(true, "Product deleted successfully");
            } else {
                return new Message(false, "Product not found");
            }
        } catch (Exception e) {
            return new Message(false, "Error in deleting the Product: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Message updateProduct(String name, Product updatedProduct) {
        try {
            Optional<Product> existingProductOptional = productRepository.findByName(name);
            if (existingProductOptional.isPresent()) {
                Product existingProduct = existingProductOptional.get();

                // Update the product fields only if they are provided (not null)
                if (updatedProduct.getName() != null) {
                    existingProduct.setName(updatedProduct.getName());
                }
                if (updatedProduct.getPrice() != null) {
                    existingProduct.setPrice(updatedProduct.getPrice());
                }
                if (updatedProduct.getActive() != null) {
                    existingProduct.setActive(updatedProduct.getActive());
                }
                if (updatedProduct.getSizes() != null) {
                    existingProduct.setSizes(updatedProduct.getSizes());
                }
                if (updatedProduct.getColors() != null) {
                    existingProduct.setColors(updatedProduct.getColors());
                }
                // Update the image if provided
                if (updatedProduct.getImage() != null) {
                    byte[] imageBytes = Base64.getDecoder().decode(updatedProduct.getImage());
                    existingProduct.setImage(imageBytes);
                }

                // Update the surface if provided
                if (updatedProduct.getSurface() != null) {
                    Surface updatedSurface = updatedProduct.getSurface();
                    Surface existingSurface = existingProduct.getSurface();

                    if (updatedSurface.getFront() != null) {
                        byte[] frontSurfaceBytes = Base64.getDecoder().decode(updatedSurface.getFront());
                        existingSurface.setFront(frontSurfaceBytes);
                    }

                    if (updatedSurface.getBack() != null) {
                        byte[] backSurfaceBytes = Base64.getDecoder().decode(updatedSurface.getBack());
                        existingSurface.setBack(backSurfaceBytes);
                    }

                    surfaceRepository.save(existingSurface);
                    existingProduct.setSurface(existingSurface);
                }

                productRepository.save(existingProduct);
                return new Message(true, "Product updated successfully");
            } else {
                return new Message(false, "Product not found");
            }
        } catch (Exception e) {
            return new Message(false, "Error in updating the Product: " + e.getMessage());
        }
    }
}
