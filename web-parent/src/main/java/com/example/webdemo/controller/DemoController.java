package com.example.webdemo.controller;

import com.example.webdemo.common.utils.Demo;
import com.example.webdemo.common.utils.GsonUtil;
import com.example.webdemo.common.utils.HttpRequestUtil;
import com.example.webdemo.common.utils.ThreadPoolUtils;
import com.example.webdemo.service.biz.DemoBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * demo
 *
 * @author admin
 * @since 2019/9/25 16:16
 */
@RestController
@RequestMapping("/api")
public class DemoController {
    @Autowired
    private DemoBiz demoBiz;

    @RequestMapping("/demo")
    public void demo() {
        demoBiz.testRpcConn();
    }

    @RequestMapping("/b")
    public void compensate() {
        demoBiz.compensate();
    }

    @RequestMapping("/c")
    public void otherDb() {
        demoBiz.otherDb();
    }

    @RequestMapping("/d")
    public Demo print(@RequestBody Demo req) {
        System.out.println("xxxxxxxxxxxxxxx:" + GsonUtil.obj2Json(req));
        Demo demo = new Demo();
        demo.setName("xx");
        demo.setAge(20);
        demo.setC(new Date());
        return demo;
    }

    @RequestMapping("/e")
    public void e() {
        ThreadPoolExecutor poolExecutor = ThreadPoolUtils.buildDefaultThreadPool(200, "test");
        for (int i = 0; i < 100; i++) {
            poolExecutor.execute(() -> {
                String url = "http://localhost:9090/api/d";
                try {
                    String s = HttpRequestUtil.sendPost(url, GsonUtil.obj2Json(new Demo()));
                    System.out.println(Thread.currentThread().getName() + "," + s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
