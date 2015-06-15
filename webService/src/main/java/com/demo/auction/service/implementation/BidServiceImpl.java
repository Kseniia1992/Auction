package com.demo.auction.service.implementation;

import com.demo.auction.entity.Bid;
import com.demo.auction.entity.ELotStatus;
import com.demo.auction.repository.IBidRepository;
import com.demo.auction.service.IBidService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class BidServiceImpl implements IBidService {

    @Autowired
    private IBidRepository bidRepository;

    final static Logger logger = Logger.getLogger(BidServiceImpl.class);

    @Override
    public List<Bid> getAll() {
        return bidRepository.findAll();
    }

    /**
     * Saving bid
     * @param bid
     * @return list of bids for lot
     */
    @Override
    @Transactional
    public List<Bid> save(Bid bid) {
        ELotStatus state = bid.getLot().getStatus();
        if(state.equals(ELotStatus.CANCELLED) || state.equals(ELotStatus.NOT_SOLD) || state.equals(ELotStatus.SOLD)){
            logger.error("Bid "+bid.getBid()+" wasn't saved. Lot's status for bid isn't active.");
            return null;
        }
        List<Bid> bidList = bidRepository.findBidsByLot(bid.getLot().getCode());
        boolean isBiggestPrice = true;
        for(Bid b : bidList){
            if(bid.getBid() <= b.getBid()){
               isBiggestPrice = false;
               break;
            }
        }
        if(bid.getBid() > bid.getLot().getStartPrice() && isBiggestPrice == true) {
            Date date = new Date();
            bid.setDate(new Timestamp(date.getTime()));
            bidRepository.save(bid);
            logger.info("Bid "+bid.getBid()+" was saved.");
            return bidRepository.findBidsByLot(bid.getLot().getCode());
        }else {
            logger.error("Bid "+ bid.getBid()+" should be greater than start price and other bids.");
            return null;
        }
    }

    /**
     * Getting bids for lot
     * @param code
     * @return list of bids
     */
    @Override
    public List findBidsByLot(Long code) {
        return bidRepository.findBidsByLot(code);
    }
}
