package com.itechart.springmusic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MusicEffectsTest {

    @Test
    void reverseEffectGeneralCaseTest() {
        File sourceFile = new File("src/test/resources/16-bit stereo.wav");
        byte[] sourceBytes = readAudioFileBytes(sourceFile);
        assertNotNull(sourceBytes);

        float[] sourceSamples = convertBytesToSamples(sourceBytes);
        MusicEffects musicEffects = new MusicEffects();
        float[] resultSamples = musicEffects.reverseEffect(sourceSamples, 44100);
        byte[] resultBytes = convertSamplesToBytes(resultSamples);

        File targetFile = new File("src/test/resources/16-bit stereo (reversed).wav");
        byte[] expectedBytes = readRegularFileBytes(targetFile);

        assertArrayEquals(expectedBytes, resultBytes);
	}

    @Test
    void reverseEffectSamplesGreaterThanFileTest() {
        File sourceFile = new File("src/test/resources/note.wav");
        byte[] sourceBytes = readAudioFileBytes(sourceFile);
        assertNotNull(sourceBytes);

        float[] sourceSamples = convertBytesToSamples(sourceBytes);
        MusicEffects musicEffects = new MusicEffects();
        float[] resultSamples = musicEffects.reverseEffect(sourceSamples, 132300);
        byte[] resultBytes = convertSamplesToBytes(resultSamples);

        File targetFile = new File("src/test/resources/note (reversed).wav");
        byte[] expectedBytes = readRegularFileBytes(targetFile);

        assertArrayEquals(expectedBytes, resultBytes);
    }

    private byte[] convertSamplesToBytes(float[] floats) {
        byte[] bytes = new byte[floats.length * 2];
        for (int i = 0, j = 0; i < floats.length; i++, j += 2) {
            short s = (short) (floats[i] * Short.MAX_VALUE);
            bytes[j + 1] = (byte) s;
            bytes[j] = (byte) (s >> 8);
        }
        return bytes;
    }

    private float[] convertBytesToSamples(byte[] bytes) {
        float[] floats = new float[bytes.length / 2];
        for (int i = 0, j = 0; i < bytes.length; i += 2, j++) {
            short s = (short) ((bytes[i] << 8) + bytes[i + 1]);
            floats[j] = (float) s / Short.MAX_VALUE;
        }
        return floats;
    }

    private byte[] readAudioFileBytes(File file) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            int contentLength = (int) (file.length() - 44);
            return stream.readNBytes(contentLength);
        } catch (IOException | UnsupportedAudioFileException e) {
        	e.printStackTrace();
		}
        return null;
	}

    private byte[] readRegularFileBytes(File file) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(44);

            int contentLength = (int) (file.length() - 44);
            byte[] bytes = new byte[contentLength];

            randomAccessFile.read(bytes, 0, contentLength);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
