package com.zb.entity;

import java.util.Date;

import lombok.Data;

@Data
public class WantOrder {
    String name;
    String id;
    String type_level1;
    String type_level2;
    String type_level3;
    String goods_num;
    String end_time;
    String type_num;
    String ship_mobile;
    String ship_addr;
    String title;
    String create_time;
                            /* "name": "空调",
           "@version": "1",
           "@timestamp": "2020-02-27T11:34:01.625Z",
           "id": 22,
           "type_level2": 2,
           "goods_num": 10,
           "end_time": "2019-06-20 15:13:47",
           "type_num": 1,
           "ship_mobile": "15105208755",
           "type_level1": 1,
           "type_level3": 1,
           "ship_addr": "江苏徐州泉山区",
           "title": "开发求购单4",
           "create_time": "2019-06-18 20:06:34"*/
}

