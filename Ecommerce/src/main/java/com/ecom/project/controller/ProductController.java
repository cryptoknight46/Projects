package com.ecom.project.controller;

import com.ecom.project.config.AppConstants;
import com.ecom.project.payload.ProductDTO;
import com.ecom.project.payload.ProductResponse;
import com.ecom.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;

        @PostMapping("/admin/catogories/{catogeryId}/product")
        public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO product,
                                                     @PathVariable Long catogeryId){

           ProductDTO savedproductDTO= productService.addProduct(catogeryId,product);
           return new ResponseEntity<>(savedproductDTO, HttpStatus.CREATED);
        }

        @GetMapping("/public/products")
        public ResponseEntity<ProductResponse> getAllProducts(
                @RequestParam(name="keyword",required=false)String keyword,
                @RequestParam(name="catogery",required = false)String catogery,
                @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sortBy,
                @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder
        ){
            ProductResponse productResponse= productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder,keyword,catogery);
            return new ResponseEntity<>(productResponse,HttpStatus.OK);
        }
        @GetMapping("/public/catogeries/{catogeryId}/products")
        public ResponseEntity<ProductResponse> getProductsByCatogery(@PathVariable Long catogeryId,
                                                                     @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                     @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                                                                     @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sortBy,
                                                                     @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder
                                                                 ){
            ProductResponse productResponse=productService.searchByCatogery(catogeryId,pageNumber,pageSize,sortBy,sortOrder);
            return new ResponseEntity<>(productResponse,HttpStatus.OK);
        }
        @GetMapping("/public/products/keyword/{keyword}/")
        public ResponseEntity<ProductResponse>getProductsByKeyword(@PathVariable String keyword,
                                                                   @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                   @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                                                                   @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sortBy,
                                                                   @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder
        ){
           ProductResponse productResponse= productService.searchProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
            return  new ResponseEntity<>(productResponse,HttpStatus.FOUND);
        }

        @PutMapping("/admin/products/{productId}")
        public ResponseEntity<ProductDTO>updateProduct(@Valid  @RequestBody ProductDTO productDTO,
                                                       @PathVariable Long productId){
           ProductDTO updatedProductDTO= productService.updateProduct(productId,productDTO);
            return new ResponseEntity<>( updatedProductDTO,HttpStatus.OK);
        }


        @DeleteMapping("/admin/products/{productId}")
        public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
            ProductDTO SavedProductDTO=
                    productService.deleteProduct(productId);

            return new ResponseEntity<>(SavedProductDTO,HttpStatus.OK);
        }

        @PutMapping("/products/{productId}/image")
        public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                             @RequestParam("image")MultipartFile image) throws IOException {
           ProductDTO updatedProduct= productService.updateProductImage(productId,image);
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
        }



}
