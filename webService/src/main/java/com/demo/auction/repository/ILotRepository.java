package com.demo.auction.repository;


import com.demo.auction.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILotRepository extends JpaRepository<Lot, Long> {
}
