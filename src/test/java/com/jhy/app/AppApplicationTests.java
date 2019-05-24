package com.jhy.app;

import com.jhy.app.common.properties.AppProperties;
import com.jhy.app.system.domain.Dept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Constants;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void test1(){
		Constants constants = new Constants(AppProperties.class);
	}

}
