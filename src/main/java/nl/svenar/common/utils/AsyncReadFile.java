package nl.svenar.common.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AsyncReadFile {

    private Path path;
    private DataBuffer dataBuffer;

    public void setFile(String filePath) {
        this.path = Paths.get(filePath);
    }

    public void read() {
        this.dataBuffer = new DataBuffer();

        AsynchronousFileChannel channel = null;
        try {
            channel = AsynchronousFileChannel.open(this.path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // start off the asynch read.
        channel.read(buffer, 0, channel, new CallbackHandler(buffer, dataBuffer));
    }

    public String getData() {
        return this.dataBuffer.getData();
    }

    public boolean isReady() {
        return this.dataBuffer.isReady();
    }

    public long getFileLength() {
        try {
            return Files.size(this.path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private class DataBuffer {
        private String data;
        private boolean doneReading;

        public void addData(String data) {
            this.data += data;
        }

        public String getData() {
            return this.data;
        }

        public void setReady(boolean ready) {
            this.doneReading = ready;
        }

        public boolean isReady() {
            return this.doneReading;
        }
    }

    private class CallbackHandler implements CompletionHandler<Integer, AsynchronousFileChannel> {

        private int position = 0;
        private ByteBuffer buffer = null;
        private DataBuffer dataBuffer;

        public CallbackHandler(ByteBuffer buffer, DataBuffer dataBuffer) {
            this.buffer = buffer;
            this.dataBuffer = dataBuffer;
        }

        @Override
        public void completed(Integer result, AsynchronousFileChannel attachment) {
            if (result != -1) {
                position += result;
                this.dataBuffer.addData(new String(buffer.array(), 0, result));

                buffer.clear();

                attachment.read(buffer, position, attachment, this);
            } else {
                this.dataBuffer.setReady(true);
            }
        }

        @Override
        public void failed(Throwable exc, AsynchronousFileChannel attachment) {
            exc.printStackTrace();
        }

    }
}
