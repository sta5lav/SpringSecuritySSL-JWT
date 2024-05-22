package com.example.springsecuritysecondhw.repository;

import com.example.springsecuritysecondhw.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    List<Ad> findAll();

    Optional<Ad> findByTitle(String title);

    Optional<Ad> findByPrice(BigDecimal price);

}
