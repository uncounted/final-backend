package com.hanghae0705.sbmoney.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    //insert into item values (1, 3000, '소세지빵', 1);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int defaultPrice;

    @NotNull
    @ManyToOne
    @JoinColumn(name="CATEGORY_ID")
    @JsonBackReference(value = "item-category-fk")
    private Category category;
}
