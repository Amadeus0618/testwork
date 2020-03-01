package com.zb.controller;

import com.zb.entity.Page;
import com.zb.entity.WantOrder;
import com.zb.service.WantOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    WantOrderService wantOrderService;

    @RequestMapping("/show")
    public Page<WantOrder> show(@RequestParam(value = "index", defaultValue = "1") Integer index,
                                @RequestParam(value = "key", required = false) String key,
                                @RequestParam(value = "type_level1", required = false) Integer type_level1,
                                @RequestParam(value = "type_level2", required = false) Integer type_level2,
                                @RequestParam(value = "type_level3", required = false) Integer type_level3,
                                @RequestParam(value = "min", required = false) Integer min,
                                @RequestParam(value = "max", required = false) Integer max) throws Exception {
        int size = 5;
        Page<WantOrder> orderByPage = wantOrderService.findOrderByPage(index, size, key, type_level1, type_level2, type_level3, min, max);
        return orderByPage;
    }

}
