package com.demo.auction.service.implementation;

import com.demo.auction.entity.AuctionUser;
import com.demo.auction.entity.EUserStatus;
import com.demo.auction.repository.IUserRepository;
import com.demo.auction.service.IUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    /**
     * Saving of user
     * @param user object
     * @return saved user
     */
    @Override
    @Transactional
    public AuctionUser save(AuctionUser user) {
        AuctionUser savingUser = userRepository.findUserByLogin(user.getLogin());
        if(savingUser == null){
            logger.info("Saved user "+user.getLogin());
            return userRepository.save(user);
        }
        logger.error("Such user "+user.getLogin()+"doesn't exist");
        return null;
    }

    /**
     * It changes user status, when user had logged in.
     * @param login
     * @param password
     * @return login user
     */
    @Override
    @Transactional
    public AuctionUser exist(String login, String password) {
        AuctionUser user = userRepository.findUserByLogin(login);
        if(user != null && user.getPassword().equals(password)){
            user.setStatus(EUserStatus.LOGIN);
            logger.info("User "+user.getLogin()+" logged in");
            return user;
        }else {
            logger.error("User "+user.getLogin()+" doesn't exist");
            return null;
        }
    }

    /**
     * It changes user status, when user had logged out.
     * @param code
     * @return
     */
    @Override
    @Transactional
    public AuctionUser edit(Long code) {
        AuctionUser user = userRepository.findOne(code);
        user.setStatus(EUserStatus.LOGOUT);
        logger.info("User "+user.getLogin()+" logged out");
        return user;
    }

}
