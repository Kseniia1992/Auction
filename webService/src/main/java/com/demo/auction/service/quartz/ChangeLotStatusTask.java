package com.demo.auction.service.quartz;

import com.demo.auction.entity.ELotStatus;
import com.demo.auction.entity.Lot;
import com.demo.auction.repository.IBidRepository;
import com.demo.auction.repository.ILotRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class ChangeLotStatusTask {

    @Autowired
    private ILotRepository lotRepository;

    @Autowired
    private IBidRepository bidRepository;

    final static Logger logger = Logger.getLogger(ChangeLotStatusTask.class);

    public void changeLotStatus(){
        logger.info("Started task execution");
        Date currentDate = new Date();
        List<Lot> lotList = lotRepository.findAll();
        if(lotList != null) {
            for (Lot lot : lotList) {
                if (lot.getStatus().equals(ELotStatus.ACTIVE) && lot.getFinishDate().before(currentDate)) {
                    if (bidRepository.findBidsByLot(lot.getCode()).size() > 0) {
                        lot.setStatus(ELotStatus.SOLD);
                        lotRepository.save(lot);
                        logger.info("Lot's status was changed. Sold. ");
                    } else {
                        lot.setStatus(ELotStatus.NOT_SOLD);
                        logger.info("Lot's status was changed. Not sold. ");
                        lotRepository.save(lot);
                    }
                }
            }
        }else {
            logger.info("No lots have been created.");
        }
    }

}
