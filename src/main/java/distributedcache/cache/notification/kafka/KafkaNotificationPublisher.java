package distributedcache.cache.notification.kafka;

import java.util.concurrent.ExecutionException;

import javax.enterprise.context.Dependent;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import distributedcache.cache.notification.Notification;
import distributedcache.cache.notification.NotificationPublisher;

/**
 * Implementation of {@link NotificationPublisher} which sends
 * {@link Notification}s through Kafka.
 * 
 * @author Philipp Buchholz
 *
 * @param <K>
 * @param <T>
 */
@Dependent
public class KafkaNotificationPublisher<K, T>
		implements NotificationPublisher<KafkaSubscription<K, Notification<T>>, T> {

	@Override
	public void publish(KafkaSubscription<K, Notification<T>> subscription, Notification<T> notification) {
		try {
			Producer<K, Notification<T>> notificationProducer = subscription.getProducer();
			notificationProducer.send(new ProducerRecord<>(subscription.outTopic().getTopicName(), notification)).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}