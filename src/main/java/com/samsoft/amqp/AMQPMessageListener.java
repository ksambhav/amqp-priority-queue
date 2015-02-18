/**
 * 
 */
package com.samsoft.amqp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author Kumar Sambhav Jain
 *
 */
public class AMQPMessageListener implements MessageListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.amqp.core.MessageListener#onMessage(org.springframework
	 * .amqp.core.Message)
	 */
	@Override
	public void onMessage(Message message) {
		LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(100));
		System.out.println(message.getMessageProperties().getPriority());
		SpringAmqpTest.countDown.countDown();
	}

}
