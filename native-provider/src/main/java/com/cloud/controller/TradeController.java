package com.cloud.controller;

import com.cloud.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @GetMapping("/send/{id}")
    public String send(@PathVariable Integer id) {
        return tradeService.sendDelayQueue(id);
    }
}
