package com.demo.auction.service;


import com.demo.auction.entity.AuctionUser;

public interface IUserService {

    AuctionUser save(AuctionUser user);
    AuctionUser exist(String login, String password);
    AuctionUser edit(Long code);

}
