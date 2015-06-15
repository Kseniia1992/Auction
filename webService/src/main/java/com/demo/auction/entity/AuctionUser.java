package com.demo.auction.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class AuctionUser extends Domain {

    @Size(min = 1, max = 20)
    private String fname;

    @Size(min = 1, max = 20)
    private String sname;

    @Size(min = 4, max = 20)
    private String login;

    @Size(min = 4, max = 20)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private EUserStatus status = EUserStatus.LOGOUT;

    @OneToMany(mappedBy = "user_lot", cascade = CascadeType.ALL)
    private List<Lot> lots;

    @OneToMany(mappedBy = "user_bid", cascade = CascadeType.ALL)
    private List<Bid> bids;

}
