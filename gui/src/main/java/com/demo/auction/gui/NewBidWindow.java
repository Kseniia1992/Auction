package com.demo.auction.gui;


import com.demo.auction.entity.Bid;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.*;

import java.util.List;

public class NewBidWindow extends Window {

    public NewBidWindow(){
        setCaption("New bid");
        setModal(true);
        center();
        createContent();
    }

    private void createContent() {
        Button okButton = new Button("Ok");
        TextField bidField = new TextField();
        bidField.addValidator(new IntegerRangeValidator("set numeric",2,1000000));
        Label label = new Label("$");
        Label bidLabel = new Label("Bid");
        HorizontalLayout bidFormLayout = new HorizontalLayout(bidLabel,bidField,label,okButton);
        bidFormLayout.setMargin(true);
        bidFormLayout.setSpacing(true);
        setContent(bidFormLayout);

        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Bid bid = new Bid();
                bid.setBid(Integer.parseInt(bidField.getValue()));
                bid.setLot(AuctionMainView.selectedRow);
                bid.setUser_bid(AuthenticationWindow.loginUser);
                String bidder = AuthenticationWindow.loginUser.getFname()+" "+AuthenticationWindow.loginUser.getSname();
                bid.setBidder(bidder);
                List<Bid> resultBid = getUI().bidService.save(bid);
                if (resultBid == null) {
                    wrongLotWarning();
                }else {
                    close();
                    AuctionMainView.bidTable.setContainerDataSource(new BeanItemContainer<Bid>(Bid.class, resultBid));
                    AuctionMainView.bidTable.setVisibleColumns(new Object[]{"bid", "date", "bidder"});
                }
            }
        });
    }

    private void wrongLotWarning(){
      Notification.show("Bid is not accepted","Wrong bid or lot's state.",
              Notification.Type.WARNING_MESSAGE);
    }

    public JpaAuctionUI getUI() {
        return (JpaAuctionUI) super.getUI();
    }
}
