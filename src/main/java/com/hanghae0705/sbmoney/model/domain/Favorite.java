package com.hanghae0705.sbmoney.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Favorite {
//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @OneToOne
    @JoinColumn(name="ITEM_ID")
    private Item item;

    @Column(nullable = false)
    private int price;

    public Favorite(Item.Request request, User user, Item item) {
        this.price = request.getDefaultPrice();
        this.user = user;
        this.item = item;
    }

    public Favorite(Request request) {
        this.user = request.getUser();
        this.item = request.getItem();
        this.price = request.getPrice();
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private User user;
        private Item item;
        private int price;

    }

    @Getter
    @NoArgsConstructor
    public static class Response {

        private User user;
        private Item item;
        private int price;
        private Category category;

        public Response(Favorite favorite){
            this.user = favorite.getUser();
            this.item = favorite.getItem();
            this.price = favorite.getPrice();

        }
    }
}
