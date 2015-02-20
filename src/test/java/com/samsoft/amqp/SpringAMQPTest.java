/**
 * 
 */
package com.samsoft.amqp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.samsoft.amqp.spring.SpringAMQPConfiguration;

/**
 * @author Kumar Sambhav Jain
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringAMQPConfiguration.class })
public class SpringAMQPTest {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private ConnectionFactory connectionFactory;

	@Before
	public void init() {

	}

	@Test
	public void testSend() {
		System.out.println("Test Send");
	}
}
