package example
import java.nio.ByteBuffer

import scala.concurrent.duration._
import scala.util.Random

import com.amazonaws.services.kinesis.producer.{KinesisProducer, KinesisProducerConfiguration}
import monix.execution.Scheduler.{global => scheduler}

object Hello extends App {
  lazy val kinesis = new KinesisProducer(new KinesisProducerConfiguration().setRegion("ap-southeast-2"));
  lazy val resources = Vector("/index.html")
  lazy val referrers = Vector("http://www.amazon.com", "http://www.google.com", "http://www.facebook.com", "http://www.reddit.com")
  lazy val random = new Random
  scheduler.scheduleWithFixedDelay(3.seconds, 30.seconds) {
    val randomResource = resources(random.nextInt(resources.length))
    val randomReferrer = referrers(random.nextInt(referrers.length))
    val data = s"""{"resource":"${randomResource}","referrer": ${randomReferrer}}""".getBytes("UTF-8")
    val result = kinesis.addUserRecord("MyStreamDataSource", "shard1", ByteBuffer.wrap(data)).get()
    println("---seeding data to kinesis-----------",result.isSuccessful())
  }
}
