package com.demo.auction.gui;


import com.demo.auction.entity.Bid;
import com.demo.auction.entity.Lot;
import com.demo.auction.service.ILotService;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
public class AuctionMainView extends VerticalLayout implements View{

    @Autowired
    ILotService lotService;

    protected static Table lotTable;
    protected static Table bidTable;
    private VerticalLayout verticalLayout;
    private VerticalLayout lotsLayout;
    private FormLayout detailsForm;

    private Label code = new Label();
    private Label lotName = new Label();
    private Label finishDate = new Label();
    private Label status = new Label();
    private Label owner = new Label();
    private Label remainingTime = new Label();
    private Label description = new Label();
    private Label startPrice = new Label();

    private Button cancelTradesButton;
    private Button bidButton;

    protected static Lot selectedRow;

    public AuctionMainView(){
        Page.getCurrent().setUriFragment("main");
        setSpacing(true);
        buildMainArea();
    }

    private void buildMainArea() {
        setSizeFull();
        setMargin(new MarginInfo(false, true, true, true));

        buildTopLayout();
        buildHorizontalLayout();
    }

    private void buildTopLayout(){
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        FileResource resource = new FileResource(new File(basepath +"/WEB-INF/image/auction.png"));
        Image image = new Image(null, resource);

        Label userLabel = new Label("User: "+AuthenticationWindow.loginUser.getFname()+" "+
                AuthenticationWindow.loginUser.getSname());

        Link logoutLink = new Link();
        logoutLink.setCaption("Logout");

        VerticalLayout linkLayout = new VerticalLayout(logoutLink);
        linkLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                getUI().userService.edit(AuthenticationWindow.loginUser.getCode());
                Page.getCurrent().reload();
            }
        });

        HorizontalLayout userLayout = new HorizontalLayout(userLabel,linkLayout);
        userLayout.setSpacing(true);

        HorizontalLayout topLayout = new HorizontalLayout(image,userLayout);
        topLayout.setWidth("100%");
        topLayout.setComponentAlignment(userLayout, Alignment.MIDDLE_RIGHT);
        addComponent(topLayout);
    }

    private void buildHorizontalLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();

        buildLotsLayout();
        buildVerticalLayout();

        horizontalLayout.addComponent(lotsLayout);
        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.setSpacing(true);

        addComponent(horizontalLayout);
        setExpandRatio(horizontalLayout,1);
    }

    private void buildLotsLayout() {
        lotsLayout = new VerticalLayout();
        lotsLayout.setSizeFull();
        lotsLayout.setCaption("Lots");
        buildLotTable();
        lotsLayout.addComponent(lotTable);
        lotsLayout.setExpandRatio(lotTable, 8);

        Button lotsButton = new Button("New lot");
        lotsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new NewLotWindow());
            }
        });

        lotsLayout.addComponent(lotsButton);
        lotsLayout.setComponentAlignment(lotsButton, Alignment.MIDDLE_RIGHT);
        lotsLayout.setExpandRatio(lotsButton, 1);
    }

    private void buildLotTable() {
        lotTable = new Table();
        lotTable.setSizeFull();
        lotTable.setImmediate(true);
        lotTable.setSelectable(true);
        lotTable.setContainerDataSource(new BeanItemContainer<Lot>(Lot.class, JpaAuctionUI.lotList));
        lotTable.setVisibleColumns(new Object[]{"code", "lotName", "finishDate", "status"});
        lotTable.setColumnHeaders(new String[]{"Code", "Lot Name", "Finish Date", "Status"});
        lotTable.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                selectedRow = (Lot) lotTable.getValue();
                if(selectedRow != null){
                    Date lotFinishDate = selectedRow.getFinishDate();
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lotFinishDate);

                    code.setValue("Code: "+ selectedRow.getCode());
                    lotName.setValue("Name: "+ selectedRow.getLotName());
                    status.setValue("State: "+selectedRow.getStatus());
                    finishDate.setValue("Finish date: "+date);
                    owner.setValue("Owner: "+selectedRow.getUser_lot().getFname()+" "+selectedRow.getUser_lot().getSname());
                    remainingTime.setValue("Remaining time: "+getUI().lotService.getRemainingTime(selectedRow.getFinishDate()));
                    description.setValue("Description: "+selectedRow.getDescription());
                    startPrice.setValue("Start price: "+selectedRow.getStartPrice()+" $");

                    bidTable.setContainerDataSource(new BeanItemContainer<Bid>(Bid.class,
                            getUI().bidService.findBidsByLot(selectedRow.getCode())));
                    bidTable.setVisibleColumns(new Object[]{"bid", "date", "bidder"});
                    bidTable.setColumnHeaders(new String[]{"Bid", "Date", "Bidder"});

                    checkLotOwner();

                    verticalLayout.setVisible(true);
                }
            }
        });
    }

    private void buildVerticalLayout() {
        verticalLayout = new VerticalLayout();
        verticalLayout.setVisible(false);
        buildDetailsForm();

        cancelTradesButton = new Button("Cancel trades");
        cancelTradesButton.setEnabled(false);
        cancelTradesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                List<Lot> editLotList = getUI().lotService.edit(selectedRow.getCode());
                AuctionMainView.lotTable.setContainerDataSource(new BeanItemContainer<Lot>(Lot.class, editLotList));
                AuctionMainView.lotTable.setVisibleColumns(new Object[]{"code", "lotName", "finishDate", "status"});
            }
        });

        buildBidTable();
        verticalLayout.addComponent(detailsForm);
        verticalLayout.addComponent(cancelTradesButton);
        verticalLayout.setComponentAlignment(cancelTradesButton,Alignment.MIDDLE_RIGHT);
        verticalLayout.addComponent(bidTable);

        bidButton = new Button("New bid");
        bidButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new NewBidWindow());
            }

        });

        verticalLayout.addComponent(bidButton);
        verticalLayout.setComponentAlignment(bidButton, Alignment.MIDDLE_RIGHT);
        verticalLayout.setExpandRatio(bidButton, 1);
        verticalLayout.setHeight("100%");
    }

    private void checkLotOwner(){
        Long lotOwnerCode = AuctionMainView.selectedRow.getUser_lot().getCode();
        if(lotOwnerCode.equals(AuthenticationWindow.loginUser.getCode())){
            bidButton.setEnabled(false);
            cancelTradesButton.setEnabled(true);
        }else {
            bidButton.setEnabled(true);
            cancelTradesButton.setEnabled(false);
        }
    }

    private void buildBidTable() {
        bidTable = new Table();
        bidTable.setWidth("100%");
        bidTable.setHeight("246px");
        bidTable.setCaption("Bids");
    }

    private void buildDetailsForm(){
        detailsForm = new FormLayout();
        detailsForm.setCaption("Lot details");
        detailsForm.addComponents(code,lotName,status,finishDate,owner,remainingTime,description,startPrice);
        detailsForm.setMargin(new MarginInfo(false,true,false,true));
    }

    public JpaAuctionUI getUI() {
        return (JpaAuctionUI) super.getUI();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
