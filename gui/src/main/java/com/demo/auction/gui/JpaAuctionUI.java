package com.demo.auction.gui;

import com.demo.auction.entity.Bid;
import com.demo.auction.entity.Lot;
import com.demo.auction.service.IBidService;
import com.demo.auction.service.ILotService;
import com.demo.auction.service.IUserService;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class JpaAuctionUI extends UI {

    @Autowired
    protected ILotService lotService;

    @Autowired
    protected IBidService bidService;

    @Autowired
    protected IUserService userService;

    protected static List<Lot> lotList;
    protected static List<Bid> bidList;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        lotList = lotService.getAll();
        bidList = bidService.getAll();
        addWindow(new AuthenticationWindow());
    }
}
