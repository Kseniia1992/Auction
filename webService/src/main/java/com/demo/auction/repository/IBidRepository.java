package com.demo.auction.repository;

import com.demo.auction.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface IBidRepository extends JpaRepository<Bid,Long> {

    @Query("select b from Bid b where b.lot.code = ?1")
    List<Bid> findBidsByLot(Long code);
}
