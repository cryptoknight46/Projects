package com.ecom.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity(name = "catogories")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Catogery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catogeryId;
    @NotBlank
    @Size(min = 5,message = "Catogery name must contain atleast 5 characters")
    private String catogeryName;

    @OneToMany(mappedBy = "catogery",cascade = CascadeType.ALL)
    private List<Product> products;

}
