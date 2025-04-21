package com.ecom.project.service;

import com.ecom.project.exceptions.ApIException;
import com.ecom.project.exceptions.ResourceNotFoundException;
import com.ecom.project.model.Cart;
import com.ecom.project.model.Catogery;
import com.ecom.project.model.Product;
import com.ecom.project.payload.CartDTO;
import com.ecom.project.payload.ProductDTO;
import com.ecom.project.payload.ProductResponse;
import com.ecom.project.repositories.CartRepository;
import com.ecom.project.repositories.CatogeryRespository;
import com.ecom.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements  ProductService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;


    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CatogeryRespository catogeryRespository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;


    @Override
    public ProductDTO addProduct(Long catogeryId, ProductDTO productDto) {
        //check if product already present or not
        Catogery catogery = catogeryRespository.findById(catogeryId).orElseThrow(
                () -> new ResourceNotFoundException("catogery", "catogeryId", catogeryId));
        boolean isProductNotPresent = true;
        List<Product> products = catogery.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDto.getProductName())) {
                isProductNotPresent = false;
                break;
            }

        }

        if (isProductNotPresent) {
            Product product = modelMapper.map(productDto, Product.class);
            product.setCatogery(catogery);
            product.setImage("default.png");
            double specialprice = product.getPrice()
                    - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialprice);
            Product savedproduct = productRepository.save(product);
            return modelMapper.map(savedproduct, ProductDTO.class);
        }
        else{
            throw new ApIException("product already exists");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String catogery) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();


         Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
         Specification<Product>spec =Specification.where(null);
        if(keyword !=null && !keyword.isEmpty()){
            spec=spec.and((root, query,
                           criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root
                    .get("productName")),"%"+keyword.toLowerCase()+"%"));
        }
        if(catogery!=null && !catogery.isEmpty()){
            spec=spec.and((root, query,
                           criteriaBuilder) -> criteriaBuilder.like(root
                    .get("catogery").get("catogeryName"),catogery));
        }


         Page<Product>pageProducts=productRepository.findAll(spec,pageDetails);
         List<Product> products = pageProducts.getContent();
         List<ProductDTO> productDTOS=products.stream().map(
                 product->{
                     ProductDTO productDTO= modelMapper.map(product, ProductDTO.class);
                     productDTO.setImage(constructImageUrl(product.getImage()));
                     return productDTO;
                 }).toList();


         ProductResponse productResponse=new ProductResponse();
         productResponse.setContent(productDTOS);
         productResponse.setPageNumber(pageProducts.getNumber());
         productResponse.setPageSize(pageProducts.getSize());
         productResponse.setTotalElements(pageProducts.getTotalElements());
         productResponse.setTotalPages(pageProducts.getTotalPages());
         productResponse.setLastpage(pageProducts.isLast());
         return productResponse;

    }

    @Override
    public ProductResponse searchByCatogery(Long catogeryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Catogery catogery = catogeryRespository.findById(catogeryId).orElseThrow(
                ()->new ResourceNotFoundException("catogery","catogeryId",catogeryId));
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product>pageProducts=productRepository.findByCatogeryOrderByPriceAsc(catogery,pageDetails);
        List<Product> products = pageProducts.getContent();
        if(products.isEmpty()){
            throw new ApIException(catogery.getCatogeryName()+"catogery does not have any products");
        }


        List<ProductDTO> productDTOS=products.stream().map(
                        product->modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastpage(pageProducts.isLast());
        return productResponse;

    }

    private String constructImageUrl(String imageName){
        return imageBaseUrl.endsWith("/")?imageBaseUrl+imageName:imageBaseUrl+ "/" + imageName;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);


        Page<Product>pageProducts=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS=products.stream().map(
                        product->modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());

        if(products.isEmpty()){
            throw new ApIException("products not foun dwith keyword : " + keyword);
        }
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastpage(pageProducts.isLast());
        return productResponse;

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        //Get existing product from db

        Product productFromDb=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("product","productId",productId));
        Product product = modelMapper.map(productDTO, Product.class);

        //update the product with user passed product
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());
        //save the product
        Product savedproduct=productRepository.save(productFromDb);
        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(savedproduct, ProductDTO.class);
    }

    public ProductDTO deleteProduct(Long productId){
        Product product=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("product","productId",productId));


        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.delete(product);
        return modelMapper.map(product,ProductDTO.class);

    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //Get the product from DB
        Product productFromDb = productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("product","productId",productId));
        //updload image to server
        //Get the filename from to the product

        String fileName=fileService.uploadImage(path,image);

        //updating the new filenanme to the product
        productFromDb.setImage(fileName);

        //save updated product
        Product updatedProduct=productRepository.save(productFromDb);
        //return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct,ProductDTO.class);

    }




}
