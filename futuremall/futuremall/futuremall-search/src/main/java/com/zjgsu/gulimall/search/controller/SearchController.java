package com.zjgsu.gulimall.search.controller;

import com.zjgsu.gulimall.search.service.MallSearchService;
import com.zjgsu.gulimall.search.vo.SearchParam;
import com.zjgsu.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    /**
     * 自动将页面提交过来的所有请求查询参数封装成指定的对象
     * @param param
     * @return
     */
    @GetMapping("/list.html")
    public String ListPage(SearchParam param, Model model){

        //1、根据传递来的页面的查询参数，去es中检索商品
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result",result);

        return "list";
    }
}
