package com.demo.auction.service.implementation;


import com.demo.auction.entity.ELotStatus;
import com.demo.auction.entity.Lot;
import com.demo.auction.repository.ILotRepository;
import com.demo.auction.service.ILotService;
import org.apache.log4j.Logger;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LotServiceImpl implements ILotService{

    @Autowired
    private ILotRepository lotRepository;

    final static Logger logger = Logger.getLogger(LotServiceImpl.class);

    @Override
    public List<Lot> getAll() {
        return lotRepository.findAll();
    }

    /**
     * Saving of lot
     * @param lot object
     * @return list of lots
     */
    @Override
    @Transactional
    public List<Lot> save(Lot lot) {
        if(lot.getFinishDate().before(new Date())){
            logger.error("Lot "+lot.getLotName()+" wasn't saved. Lot's status isn't active.");
            return null;
        }else {
            lotRepository.save(lot);
            logger.info("Lot "+lot.getLotName()+" was saved.");
            return lotRepository.findAll();
        }
    }

    /**
     * Canceling of lot
     * @param code
     * @return list of lots
     */
    @Override
    @Transactional
    public List<Lot> edit(Long code) {
        Lot lot = lotRepository.findOne(code);
        lot.setStatus(ELotStatus.CANCELLED);
        logger.info("Lot "+lot.getCode()+" was cancelled");
        return lotRepository.findAll();
    }

    /**
     * Getting the remaining time
     * @param finishDate
     * @return remaining time
     */
    @Override
    public String getRemainingTime(Date finishDate) {
        DateTime currentDateTime = new DateTime(new Date());
        DateTime finishDateTime = new DateTime(finishDate);

        String days = Days.daysBetween(currentDateTime,finishDateTime).getDays() +" days, ";
        String hours = Hours.hoursBetween(currentDateTime,finishDateTime).getHours() % 24 +" hours, ";
        String minutes = Minutes.minutesBetween(currentDateTime,finishDateTime).getMinutes() % 60 + " minutes, ";
        String seconds = Seconds.secondsBetween(currentDateTime,finishDateTime).getSeconds() % 60 + " seconds.";
        String result = days + hours + minutes + seconds;

        return result;
    }
}
