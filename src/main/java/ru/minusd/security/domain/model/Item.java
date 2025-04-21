package ru.minusd.security.domain.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;
    @Column(name="price")
    private Integer price;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<History> histories;

    public Item(String name, Integer price){
        this.name=name;
        this.price=price;
    }
}
