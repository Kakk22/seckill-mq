package com.cyf;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 陈一锋
 * @date 2021/3/27 16:16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @org.junit.Test
    public void test1() {
        productService.deStock(1L);
    }

    @org.junit.Test
    public void test2(){
        productMapper.deStockByLock(1L);
    }
}
