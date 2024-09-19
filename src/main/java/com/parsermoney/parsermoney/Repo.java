package com.parsermoney.parsermoney;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Repo  extends JpaRepository<Money, Long> {
    Optional<Money> findByTypeMoney(String typeMoney);
}
