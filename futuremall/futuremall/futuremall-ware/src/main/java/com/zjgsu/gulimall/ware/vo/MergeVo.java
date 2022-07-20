package com.zjgsu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class MergeVo {
    private Long purchaseId;//整单id
    public List<Long> items;//[1,2,3,4]//合并项集合
}
