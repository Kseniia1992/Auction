package com.demo.auction.gui;


import com.demo.auction.entity.AuctionUser;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class RegistrationWindow extends Window{

    private AuctionUser user;

    public RegistrationWindow(){
        Page.getCurrent().setUriFragment("register");
        setCaption("Registration");
        center();
        buildForm();
    }

    private void buildForm() {
        Button registerButton = new Button("Register");
        Button cancelButton = new Button("Cancel");
        HorizontalLayout actions = new HorizontalLayout(registerButton,cancelButton);
        actions.setSpacing(true);

        final TextField login = new TextField("Login");
        login.addValidator(new BeanValidator(AuctionUser.class,"login"));
        final TextField password = new TextField("Password");
        password.addValidator(new BeanValidator(AuctionUser.class,"password"));
        final TextField fname = new TextField("First name");
        fname.addValidator(new BeanValidator(AuctionUser.class,"fname"));
        final TextField sname = new TextField("Last name");
        sname.addValidator(new BeanValidator(AuctionUser.class,"sname"));

        FormLayout regForm = new FormLayout(login,password,fname,sname,actions);
        regForm.setSpacing(true);
        regForm.setMargin(true);

        setContent(regForm);

        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        registerButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                user = new AuctionUser();
                user.setLogin(login.getValue());
                user.setPassword(password.getValue());
                user.setFname(fname.getValue());
                user.setSname(sname.getValue());
                AuctionUser savedUser = getUI().userService.save(user);
                if(savedUser == null){
                    registrationFaild();
                }else {
                    close();
                }
            }
        });
    }

    private void registrationFaild(){
        Notification.show("Registration failed","Such user has already existed.",
                Notification.Type.ERROR_MESSAGE);
    }

    public JpaAuctionUI getUI() {
        return (JpaAuctionUI) super.getUI();
    }
}
