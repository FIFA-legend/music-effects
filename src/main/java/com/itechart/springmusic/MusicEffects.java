package com.itechart.springmusic;

import org.apache.commons.lang3.ArrayUtils;

public class MusicEffects {

    /**
     * Creates a new array of samples where first {@code samplesPerChannelToReverse}
     * per each channel are written in the reverse order. If {@code samplesPerChannelToReverse > samples.length / 2}
     * then the {@code samples} array is filled with zeros to fit the size.
     *
     * @param samples array of samples that should be reversed.
     * @param samplesPerChannelToReverse amount of samples to reverse in each channel.
     * @return a new array of samples at which first {@code samplesPerChannelToReverse} elements are reversed.
     * @throws IllegalArgumentException if {@code samplesPerChannelToReverse} is negative.
     */
    public float[] reverseEffect(float[] samples, int samplesPerChannelToReverse) {
        if (samplesPerChannelToReverse < 0) {
            throw new IllegalArgumentException("Sample per each channel must not be negative");
        }

        float[] leftChannelSamples = extractChannel(samples, samplesPerChannelToReverse, 0);
        float[] rightChannelSamples = extractChannel(samples, samplesPerChannelToReverse, 1);

        ArrayUtils.reverse(leftChannelSamples, 0, samplesPerChannelToReverse);
        ArrayUtils.reverse(rightChannelSamples, 0, samplesPerChannelToReverse);

        return joinChannels(leftChannelSamples, rightChannelSamples);
    }

    private float[] extractChannel(float[] samples, int samplesPerChannelToReverse, int channel) {
        int channelLength = Math.max(samplesPerChannelToReverse, samples.length / 2);
        float[] channelSamples = new float[channelLength];
        for (int i = channel, j = 0; i < samples.length && j < channelLength; i += 2, j++) {
            channelSamples[j] = samples[i];
        }
        return channelSamples;
    }

    private float[] joinChannels(float[] leftChannelSamples, float[] rightChannelSamples) {
        float[] reversedSamples = new float[leftChannelSamples.length + rightChannelSamples.length];
        for (int i = 0, j = 0; i < leftChannelSamples.length; i++, j += 2) {
            reversedSamples[j] = leftChannelSamples[i];
            reversedSamples[j + 1] = rightChannelSamples[i];
        }
        return reversedSamples;
    }

}
