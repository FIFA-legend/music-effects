package com.itechart.springmusic;

public class MusicEffects {

    /**
     * Creates a new array of samples where first {@code samplesPerChannelToReverse}
     * per each channel are written in the reverse order. If {@code samplesPerChannelToReverse > samples.length}
     * then the whole array is reversed.
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

        int samplesPerTrackToReverse = Math.min(samples.length, 2 * samplesPerChannelToReverse);
        float[] samplesToReverse = extractSamplesToReverse(samples, samplesPerTrackToReverse);
        reverseSamples(samplesToReverse);

        float[] processedSamples = new float[samples.length];
        System.arraycopy(samplesToReverse, 0, processedSamples, 0, samplesPerTrackToReverse);
        if (samplesPerTrackToReverse < processedSamples.length) {
            int copyCount = processedSamples.length - samplesPerTrackToReverse;
            System.arraycopy(samples, samplesPerTrackToReverse, processedSamples, samplesPerTrackToReverse, copyCount);
        }
        return processedSamples;
    }

    private float[] extractSamplesToReverse(float[] samples, int samplesPerTrackToReverse) {
        float[] samplesToReverse = new float[samplesPerTrackToReverse];
        System.arraycopy(samples, 0, samplesToReverse, 0, samplesPerTrackToReverse);
        return samplesToReverse;
    }

    private void reverseSamples(float[] samplesToReverse) {
        reverseChannel(samplesToReverse, 0, samplesToReverse.length - 2);
        reverseChannel(samplesToReverse, 1, samplesToReverse.length - 1);
    }

    private void reverseChannel(float[] samplesToReverse, int startIndex, int endIndex) {
        float temp;
        while (startIndex < endIndex) {
            temp = samplesToReverse[startIndex];
            samplesToReverse[startIndex] = samplesToReverse[endIndex];
            samplesToReverse[endIndex] = temp;

            startIndex += 2;
            endIndex -= 2;
        }
    }

}
