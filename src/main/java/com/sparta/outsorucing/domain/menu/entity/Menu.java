package com.sparta.outsorucing.domain.menu.entity;

import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.domain.store.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name="menu")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "메뉴 고유번호")
    private Long id;

    @Column(nullable = false)
    @Comment(value = "메뉴명")
    private String menuName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String content;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Store store;

    @Builder
    public Menu(String menuName, int price, String content, Status status, Store store) {
        this.menuName = menuName;
        this.price = price;
        this.content = content;
        this.status = status;
        this.store = store;
    }
}
