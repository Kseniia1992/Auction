package com.demo.auction.gui;

import com.demo.auction.entity.Lot;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

import java.util.List;

public class NewLotWindow extends Window implements Button.ClickListener {

    private Button saveButton;
    private Button cancelButton;
    private TextField lotName;
    private DateField finishDate;
    private TextField startPrice;
    private TextArea description;

    public NewLotWindow(){
        setCaption("New lot");
        setModal(true);
        center();
        createContent();
    }

    public void createContent(){

        saveButton = new Button("Create",this);
        cancelButton = new Button("Cancel",this);

        HorizontalLayout actions = new HorizontalLayout(saveButton,cancelButton);
        actions.setMargin(true);
        actions.setSpacing(true);

        FormLayout fields = new FormLayout();
        fields.setMargin(true);
        fields.setSpacing(true);
        lotName = new TextField("Lot name");
        lotName.addValidator(new BeanValidator(Lot.class,"lotName"));
        finishDate = new DateField("Finish date");
        finishDate.setResolution(Resolution.SECOND);
        startPrice = new TextField("Start price");
        startPrice.addValidator(new IntegerRangeValidator("set numeric",1,1000000));
        description = new TextArea("Description");
        fields.addComponents(lotName,finishDate,startPrice,description, actions);
        setContent(fields);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        if (clickEvent.getButton() == cancelButton) {
            close();
        }else if(clickEvent.getButton() == saveButton) {
            Lot lot = new Lot();
            lot.setLotName(lotName.getValue());
            lot.setFinishDate(finishDate.getValue());
            lot.setStartPrice(Integer.parseInt(startPrice.getValue()));
            lot.setDescription(description.getValue());
            lot.setUser_lot(AuthenticationWindow.loginUser);
            String owner = AuthenticationWindow.loginUser.getFname()+" "+AuthenticationWindow.loginUser.getSname();
            lot.setOwner(owner);
            List<Lot> lotResult = getUI().lotService.save(lot);
            if(lotResult == null){
                wrongDateWarning();
            }else {
                close();
                AuctionMainView.lotTable.setContainerDataSource(new BeanItemContainer<Lot>(Lot.class, lotResult));
                AuctionMainView.lotTable.setVisibleColumns(new Object[]{"code", "lotName", "finishDate", "status"});
            }
        }
    }

    private void wrongDateWarning(){
        Notification.show("Lot is not accepted","Wrong lot's finish date.",
                Notification.Type.WARNING_MESSAGE);
    }

    public JpaAuctionUI getUI() {
        return (JpaAuctionUI) super.getUI();
    }
}
