package com.atguigu.gmall.test;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageBuilderTest {

    @Test
    public void testGetMessage1() {
        MessageBuilder obj = new MessageBuilder();
        assertEquals("Hello test", obj.getMessage("test"));
    }

}


