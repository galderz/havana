package j.aeron;

public class SampleConfiguration {
   public static final String CHANNEL_PROP = "aeron.sample.channel";
   public static final String STREAM_ID_PROP = "aeron.sample.streamId";

   public static final String PING_CHANNEL_PROP = "aeron.sample.ping.channel";
   public static final String PONG_CHANNEL_PROP = "aeron.sample.pong.channel";
   public static final String PING_STREAM_ID_PROP = "aeron.sample.ping.streamId";
   public static final String PONG_STREAM_ID_PROP = "aeron.sample.pong.streamId";
   public static final String WARMUP_NUMBER_OF_MESSAGES_PROP = "aeron.sample.warmup.messages";
   public static final String WARMUP_NUMBER_OF_ITERATIONS_PROP = "aeron.sample.warmup.iterations";
   public static final String RANDOM_MESSAGE_LENGTH_PROP = "aeron.sample.randomMessageLength";

   public static final String FRAME_COUNT_LIMIT_PROP = "aeron.sample.frameCountLimit";
   public static final String MESSAGE_LENGTH_PROP = "aeron.sample.messageLength";
   public static final String NUMBER_OF_MESSAGES_PROP = "aeron.sample.messages";
   public static final String LINGER_TIMEOUT_MS_PROP = "aeron.sample.lingerTimeout";
   public static final String EMBEDDED_MEDIA_DRIVER_PROP = "aeron.sample.embeddedMediaDriver";
   public static final String EXCLUSIVE_PUBLICATIONS_PROP = "aeron.sample.exclusive.publications";

   public static final String INFO_FLAG_PROP = "aeron.sample.info";

   public static final String CHANNEL;
   public static final String PING_CHANNEL;
   public static final String PONG_CHANNEL;

   public static final boolean EMBEDDED_MEDIA_DRIVER;
   public static final boolean RANDOM_MESSAGE_LENGTH;
   public static final boolean INFO_FLAG;
   public static final int STREAM_ID;
   public static final int PING_STREAM_ID;
   public static final int PONG_STREAM_ID;
   public static final int FRAGMENT_COUNT_LIMIT;
   public static final int MESSAGE_LENGTH;
   public static final int WARMUP_NUMBER_OF_ITERATIONS;
   public static final long WARMUP_NUMBER_OF_MESSAGES;
   public static final long NUMBER_OF_MESSAGES;
   public static final long LINGER_TIMEOUT_MS;
   public static final boolean EXCLUSIVE_PUBLICATIONS;

   static
   {
      CHANNEL = System.getProperty(CHANNEL_PROP, "aeron:udp?endpoint=localhost:40123");
      STREAM_ID = Integer.getInteger(STREAM_ID_PROP, 10);
      PING_CHANNEL = System.getProperty(PING_CHANNEL_PROP, "aeron:udp?endpoint=localhost:40123");
      PONG_CHANNEL = System.getProperty(PONG_CHANNEL_PROP, "aeron:udp?endpoint=localhost:40124");
      LINGER_TIMEOUT_MS = Long.getLong(LINGER_TIMEOUT_MS_PROP, 0);
      PING_STREAM_ID = Integer.getInteger(PING_STREAM_ID_PROP, 10);
      PONG_STREAM_ID = Integer.getInteger(PONG_STREAM_ID_PROP, 10);
      FRAGMENT_COUNT_LIMIT = Integer.getInteger(FRAME_COUNT_LIMIT_PROP, 10);
      MESSAGE_LENGTH = Integer.getInteger(MESSAGE_LENGTH_PROP, 32);
      RANDOM_MESSAGE_LENGTH = Boolean.getBoolean(RANDOM_MESSAGE_LENGTH_PROP);
      NUMBER_OF_MESSAGES = Long.getLong(NUMBER_OF_MESSAGES_PROP, 10_000_000);
      WARMUP_NUMBER_OF_MESSAGES = Long.getLong(WARMUP_NUMBER_OF_MESSAGES_PROP, 10_000);
      WARMUP_NUMBER_OF_ITERATIONS = Integer.getInteger(WARMUP_NUMBER_OF_ITERATIONS_PROP, 5);
      EMBEDDED_MEDIA_DRIVER = Boolean.getBoolean(EMBEDDED_MEDIA_DRIVER_PROP);
      INFO_FLAG = Boolean.getBoolean(INFO_FLAG_PROP);
      EXCLUSIVE_PUBLICATIONS = Boolean.getBoolean(EXCLUSIVE_PUBLICATIONS_PROP);
   }
}
