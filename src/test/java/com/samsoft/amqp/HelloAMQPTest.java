package com.samsoft.amqp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author Kumar Sambhav Jain
 *
 */
public class HelloAMQPTest {

	/**
	 * 
	 */
	private static final int NO_OF_MESSAGES = 100;
	private static final String QUEUE_NAME = "theQueue37";
	private ConnectionFactory factory;
	private com.rabbitmq.client.Connection connection;
	private Channel channel;

	@Before
	public void init() throws IOException {
		factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setUsername("guest");
		factory.setPassword("guest");
		connection = factory.newConnection();
		channel = connection.createChannel();

		// dead exchange
		channel.exchangeDeclare("dead-exchange", "direct", true);

		Map<String, Object> map = new HashMap<>();
		map.put("x-dead-letter-exchange", "dead-exchange");
		map.put("x-dead-letter-routing-key", "dead-key");
		map.put("x-max-priority", 3);
		channel.queueDeclare(QUEUE_NAME, true, false, false, map);
	}

	//@Test
	public void testSend() throws IOException {
		Assert.assertNotNull(channel);
		for (int i = 0; i < NO_OF_MESSAGES; i++) {
			String message = "Hello World! " + i;
			BasicProperties.Builder builder = new Builder();
			builder.priority(i % 5 == 0 ? 1 : 2);
			channel.basicPublish("", QUEUE_NAME, builder.build(),
					message.getBytes());
		}
	}

	@Test
	public void testRecieve() throws IOException, ShutdownSignalException,
			ConsumerCancelledException, InterruptedException {

		boolean autoAck = true;
		DefaultConsumer callback = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println(" [x] Received '" + new String(body) + "' "+properties.getPriority());
				//channel.basicAck(envelope.getDeliveryTag(), false);
				// channel.basicNack(envelope.getDeliveryTag(), false, false);
			}
		};
		while (true) {
			channel.basicConsume(QUEUE_NAME, autoAck, callback);
			Thread.sleep(2000);
		}
	}

}
