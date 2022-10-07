package com.itechart.springmusic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MusicEffectsTest {

    @Test
    void reverseEffectTest() {
        File actualFile = new File("src/test/resources/16-bit stereo.wav");
        File expectedFile = new File("src/test/resources/16-bit stereo (reversed).wav");

        AudioInputStream actualStream = loadAudio(actualFile);
		assertNotNull(actualStream);

        byte[] startByteSamples = readBytes(actualStream, (int) actualFile.length());
        assertNotNull(startByteSamples);

        float[] startFloatSamples = convertToFloatSamples(startByteSamples);
        MusicEffects musicEffects = new MusicEffects();
        float[] resultFloatSamples = musicEffects.reverseEffect(startFloatSamples, 44100);
        byte[] resultByteSamples = convertToByteSamples(resultFloatSamples);

        AudioInputStream expectedStream = loadAudio(expectedFile);
        assertNotNull(expectedStream);

        byte[] expectedBytes = readBytes(expectedStream, (int) expectedFile.length());
        assertArrayEquals(expectedBytes, resultByteSamples);
	}

    private byte[] convertToByteSamples(float[] floats) {
        byte[] bytes = new byte[floats.length * 2];
        for (int i = 0, j = 0; i < floats.length; i++, j += 2) {
            short s = (short) (floats[i] * Short.MAX_VALUE);
            bytes[j + 1] = (byte) s;
            bytes[j] = (byte) (s >> 8);
        }
        return bytes;
    }

    private float[] convertToFloatSamples(byte[] bytes) {
        float[] floats = new float[bytes.length / 2];
        for (int i = 0, j = 0; i < bytes.length; i += 2, j++) {
            short s = (short) ((bytes[i] << 8) + bytes[i + 1]);
            floats[j] = (float) s / Short.MAX_VALUE;
        }
        return floats;
    }

    private byte[] readBytes(AudioInputStream stream, int fileLength) {
        try {
            int contentLength = fileLength - 44;
            return stream.readNBytes(contentLength);
        } catch (IOException e) {
        	e.printStackTrace();
		}
		return null;
	}

    private AudioInputStream loadAudio(File file) {
        try {
			AudioInputStream source = AudioSystem.getAudioInputStream(file);
			AudioFormat sourceFormat = source.getFormat();

            AudioFormat targetFormat = convertTo16BitFormat(sourceFormat);
            return AudioSystem.getAudioInputStream(targetFormat, source);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AudioFormat convertTo16BitFormat(AudioFormat format) {
        int sampleSizeInBytes = 2;
        return new AudioFormat(
                format.getEncoding(),
                format.getSampleRate(),
                sampleSizeInBytes * 8,
                format.getChannels(),
                format.getChannels() * sampleSizeInBytes,
                format.getFrameRate(),
                format.isBigEndian()
        );
    }
}
