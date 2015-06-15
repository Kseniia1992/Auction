package com.demo.auction.gui;


import com.demo.auction.entity.AuctionUser;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class AuthenticationWindow extends Window {

    protected static AuctionUser loginUser;

    public AuthenticationWindow(){
        Page.getCurrent().setUriFragment("login");
        setCaption("Authentication");
        center();
        setClosable(false);
        buildForm();
    }

    private void buildForm() {
        Button loginButton = new Button("Login");
        Link registerLink = new Link();
        registerLink.setCaption("Register");
        HorizontalLayout linkLayout = new HorizontalLayout();
        linkLayout.addComponent(registerLink);
        linkLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                UI.getCurrent().addWindow(new RegistrationWindow());
                setEnabled(true);
            }
        });

        TextField login = new TextField("Login");
        login.setInputPrompt("Login");
        PasswordField password = new PasswordField("Password");
        password.setInputPrompt("Password");

        HorizontalLayout actions = new HorizontalLayout(loginButton,linkLayout);
        actions.setSpacing(true);

        FormLayout authForm = new FormLayout(login,password,actions);
        authForm.setMargin(true);
        authForm.setSpacing(true);

        setContent(authForm);

        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                loginUser = getUI().userService.exist(login.getValue(),password.getValue());
                if(loginUser == null){
                    authenticationFaild();
                }else {
                    close();
                    UI.getCurrent().setContent(new AuctionMainView());
                }
            }
        });
    }

    private void authenticationFaild(){
        Notification.show("Authentication failed","Wrong login or password. Please try again or register.",
                Notification.Type.ERROR_MESSAGE);
    }

    public JpaAuctionUI getUI() {
        return (JpaAuctionUI) super.getUI();
    }

}
