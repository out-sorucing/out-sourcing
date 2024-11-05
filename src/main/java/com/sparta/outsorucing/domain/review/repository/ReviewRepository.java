package com.sparta.outsorucing.domain.review.repository;

import com.sparta.outsorucing.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN r.order o WHERE o.store.id = :storeId ORDER BY r.createdAt DESC")
    List<Review> findByStoreIdOrderByCreatedAtDesc(@Param("storeId") Long storeId);
    @Query("SELECT r FROM Review r JOIN r.order o WHERE o.store.id = :storeId AND r.rating <= :rating " +
            "ORDER BY r.createdAt DESC")
    List<Review> findByStoreIdAndRating(@Param("storeId") Long storeId, @Param("rating") int rating);
}
