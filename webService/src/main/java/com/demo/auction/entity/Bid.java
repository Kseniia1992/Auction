package com.demo.auction.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Bid extends Domain{

    private int bid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String bidder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AuctionUser user_bid;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;

}
