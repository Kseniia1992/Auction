package com.demo.auction.service;


import com.demo.auction.entity.Bid;

import java.util.List;

public interface IBidService {

    List<Bid> getAll();
    List<Bid> save(Bid bid);
    List findBidsByLot(Long code);
}
