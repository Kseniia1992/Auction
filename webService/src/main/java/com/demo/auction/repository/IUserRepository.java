package com.demo.auction.repository;

import com.demo.auction.entity.AuctionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IUserRepository extends JpaRepository<AuctionUser, Long>{

    @Query("select u from AuctionUser u where u.login = ?1")
    AuctionUser findUserByLogin(String login);
}
