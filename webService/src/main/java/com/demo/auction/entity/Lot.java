package com.demo.auction.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Lot extends Domain{

    @Size(min = 1, max = 20)
    private String lotName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;

    private String owner;

    @NotNull
    private int startPrice;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private ELotStatus status = ELotStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AuctionUser user_lot;

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL)
    private List<Bid> lot_bids;

}
