package com.demo.auction.service;

import com.demo.auction.entity.Lot;

import java.util.Date;
import java.util.List;

public interface ILotService {

    List<Lot> getAll();
    List<Lot> save(Lot lot);
    List<Lot> edit(Long code);
    String getRemainingTime(Date finishDate);

}
