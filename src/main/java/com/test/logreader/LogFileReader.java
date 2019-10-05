package com.test.logreader;

import ch.qos.logback.classic.Logger;
import com.test.exception.CriticalAppException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class LogFileReader {
    private static final int BUFFER_SIZE = 500;
    private String filePath;
    private long position = 0;

    public LogFileReader(String filePath) {
        this.filePath = filePath;
    }

    public Optional<String> getChunkToProcess() throws CriticalAppException {
        try (SeekableByteChannel channel = Files.newByteChannel(Paths.get(filePath), StandardOpenOption.READ)) {
            channel.position(position);
            StringBuilder line = new StringBuilder();
            if (!readMainPartOfBuffor(channel, line)) return Optional.empty();
            readTextTillTheEndOfCurrentEvent(channel, line);
            updatePosition(channel);
            return Optional.of(normalizeChunk(line));
        } catch (IOException e) {
            throw new CriticalAppException("Problem with getting chunk " + e.toString());
        }
    }

    private String normalizeChunk(StringBuilder line) {
        return line.toString().replaceAll("(\\r|\\n|\\t)", "");
    }

    private void updatePosition(SeekableByteChannel channel) throws IOException {
        position = channel.position();
    }

    private void readTextTillTheEndOfCurrentEvent(SeekableByteChannel channel, StringBuilder line) throws IOException {
        ByteBuffer oneChar = ByteBuffer.allocate(1);
        while (channel.read(oneChar) != -1) {
            char c = (char) oneChar.get(0);
            line.append(c);
            oneChar.clear();
            if (isSignOfEventEnding(c)) {
                break;
            }
        }
    }

    private boolean isSignOfEventEnding(char c) {
        return c == '}';
    }

    private boolean readMainPartOfBuffor(SeekableByteChannel channel, StringBuilder line) throws IOException {
        ByteBuffer bb = ByteBuffer.allocateDirect(BUFFER_SIZE);

        int n = channel.read(bb);

        if (isBytesReadSuccessfully(n, -1)){
            return false;
        }

        for (int i = 0; i < n; i++) {
            line.append((char) bb.get(i));
        }
        bb.clear();

        return true;
    }

    private boolean isBytesReadSuccessfully(int n, int i) {
        return n == i;
    }
}
