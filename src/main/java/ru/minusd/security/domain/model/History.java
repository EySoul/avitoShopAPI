package ru.minusd.security.domain.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

@Entity
@Data
@Table(name = "history")
@NoArgsConstructor
public class History {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    @JsonBackReference
    private Item item;
    private Integer amount;

    public History(User user, Item item, Integer amount){
        this.user = user;
        this.item = item;
        this.amount = amount;
    }
}
