package com.ecom.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products")
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @Size(min=3,message = "product name should contain atleast three characters")
    private String productName;

    @Size(min=3,message = "product name should contain atleast three characters")
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    private String image;


    @ManyToOne
    @JoinColumn(name="catogery_id")
    private Catogery catogery;

   // @OneToMany(mappedBy = "catogery",cascade=CascadeType.ALL)
    //private List<Product>products;
    @ManyToOne
    @JoinColumn(name="seller_id")
    private User user;

    @OneToMany(mappedBy = "product" ,cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.EAGER)
    private List<CartItem> products= new ArrayList<>();


}
