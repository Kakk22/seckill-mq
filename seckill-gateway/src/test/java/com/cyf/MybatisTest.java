package com.cyf;

import com.cyf.domain.Product;
import com.cyf.mapper.ProductMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author 陈一锋
 * @date 2021/3/12 16:12
 **/
@RunWith(SpringRunner.class)
@SpringBootTest()
public class MybatisTest {

    @Resource
    private ProductMapper productMapper;

    @Test
    public void t1(){
        Product product = productMapper.selectById(1);
        System.out.println(product);
    }
}
