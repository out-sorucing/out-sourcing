package com.sparta.outsorucing.domain.favorites.entity;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.store.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name="out_favorites")
@NoArgsConstructor
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "즐겨찾기 고유번호")
    private Long id;

    @Column
    @Comment(value = "가게명")
    private String storeName;

    @Column
    private String openTime;

    @Column
    private String closeTime;

    @Column
    private int minPrice;

    @Column
    private Long memberId;

    @Column
    private Long storeId;

    public Favorites(Long storeId, Long memberId, String storeName, String openTime,
        String closeTime, int minPrice) {
        this.storeId = storeId;
        this.memberId = memberId;
        this.storeName = storeName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minPrice = minPrice;
    }
}



