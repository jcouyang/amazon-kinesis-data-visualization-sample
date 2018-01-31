package example

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.{ IRecordProcessor, IRecordProcessorFactory }
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.{ KinesisClientLibConfiguration, ShutdownReason, Worker }
import com.amazonaws.services.kinesis.clientlibrary.types.{ InitializationInput, ProcessRecordsInput, ShutdownInput }
import com.amazonaws.services.kinesis.model.Record
import java.util.UUID
import scala.collection.JavaConversions._

object Hello extends App {
  lazy val workerId = UUID.randomUUID();
  lazy val kinesisClientLibConfiguration =
    new KinesisClientLibConfiguration("KCLDynamoDBTable",
      "MyStreamDataSource",
      new ProfileCredentialsProvider(),
      workerId.toString).withRegionName("ap-southeast-2");
  val worker = new Worker.Builder()
    .recordProcessorFactory(new RecordProcessorFactory())
    .config(kinesisClientLibConfiguration)
    .build();
  worker.run()
}


class RecordProcessorFactory extends IRecordProcessorFactory {
  override def createProcessor() = new SampleRecordProcessor
}

class SampleRecordProcessor extends IRecordProcessor {
  var kinesisShardId:String = "undefined"

  override def initialize(initializationInput: InitializationInput) = {
    kinesisShardId = initializationInput.getShardId()
  }

  override def processRecords(processRecordsInput: ProcessRecordsInput) = {
    process(processRecordsInput.getRecords.toList)
    val checkpointer = processRecordsInput.getCheckpointer()
    println(s"Checkpointing shard ${kinesisShardId}")
    checkpointer.checkpoint()
  }

  override def shutdown(shutdownInput: ShutdownInput) = {
    println("Shutting down record processor " + kinesisShardId);
    if (shutdownInput.getShutdownReason == ShutdownReason.TERMINATE) {
      shutdownInput.getCheckpointer.checkpoint();
    }
  }

  def process(records: List[Record]) = {
    records.foreach { record => println(s"Processing ${record.getData} in ${record.getPartitionKey}") }
  }
}
