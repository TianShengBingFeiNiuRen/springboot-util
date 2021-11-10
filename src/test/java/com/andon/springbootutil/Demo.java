package com.andon.springbootutil;

import com.andon.springbootutil.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author Andon
 * 2021/11/10
 */
@Slf4j
public class Demo {

    @Test
    public void test01() {
        String id = RandomUtil.generateID();
        log.info("id:{}", id);
    }
}
