package com.zjgsu.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {
    //父分类id
    private String catalog1Id;

    private String id;

    private String name;

    private List<Catelog3Vo> catelog3List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo{
        private String catalog2Id;
        private String id;
        private String name;
    }
}
