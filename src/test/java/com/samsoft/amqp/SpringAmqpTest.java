/**
 * 
 */
package com.samsoft.amqp;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Kumar Sambhav Jain
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:amqp-appContext.xml" })
public class SpringAmqpTest {

	@Autowired
	private AmqpTemplate msgTemplate;
	
	public static final int size = 100;
	
	public static final CountDownLatch countDown = new CountDownLatch(size);
	@Test
	public void test() throws Exception {
	
		for(int i = 1; i<=100;++i){
			Object msg = "TestMessage"+i;
			msgTemplate.convertAndSend("q1", msg,getMessagePostProcessor(i));
		}
		System.out.println("Sent all messages");
		countDown.await();
	}

	/**
	 * @return
	 */
	private MessagePostProcessor getMessagePostProcessor(int index) {
		int priority = index % 10 ==0 ? 10: 5;
		return new CustomMessagePostProcessor(priority);
	}

	private static class CustomMessagePostProcessor implements MessagePostProcessor{
		
		private final int priority;
		
		public CustomMessagePostProcessor(int priority) {
			super();
			this.priority = priority;
		}

		/* (non-Javadoc)
		 * @see org.springframework.amqp.core.MessagePostProcessor#postProcessMessage(org.springframework.amqp.core.Message)
		 */
		@Override
		public Message postProcessMessage(Message message) throws AmqpException {
			message.getMessageProperties().setPriority(priority);
			return message;
		}
		
	}
}
