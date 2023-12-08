package de.trodel.soundboard.server.broadcast;

import static java.net.StandardProtocolFamily.INET;
import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class BroadcastChannelServer implements AutoCloseable {
    private static final String BROAD_CAST_RESPONSE = "soundboard server here";

    private final DatagramChannel channel;
    private final ExecutorService executor;

    public BroadcastChannelServer(int port) throws IOException {
        channel = DatagramChannel.open(INET);
        channel.configureBlocking(true);
        channel.bind(new InetSocketAddress(port));
        executor = newSingleThreadExecutor();

        executor.submit(generateWorker());
    }

    private Callable<Void> generateWorker() {
        return () -> {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            try {
                while (true) {
                    SocketAddress client = channel.receive(buffer);
                    buffer.clear();
                    channel.send(wrap(BROAD_CAST_RESPONSE.getBytes(UTF_8)), client);
                }
            } catch (IOException e) {
                return null;
            }

        };
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
        channel.close();
        if (!executor.awaitTermination(1, SECONDS)) {
            executor.shutdownNow();
        }
    }

}
