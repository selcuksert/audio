package com.corp.concepts.audio.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class AudioConverterService {
	private static final Logger logger = LoggerFactory.getLogger(AudioConverterService.class);

	public Flux<byte[]> generateAudioStream(String filePath, float sampleRate, int sampleSizeInBits, int channels) {
		boolean signed = true;
		boolean bigEndian = true;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			byte[] fisByteArray = IOUtils.toByteArray(fis);
			ByteArrayInputStream audioStream = new ByteArrayInputStream(fisByteArray);
			AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

			logger.debug(String.format("Generated audio stream with sampling rate: %f", sampleRate));

			Flux<byte[]> audioFlux = Flux.fromStream(Stream.generate(() -> {
				try {
					return new AudioInputStream(audioStream, format, fisByteArray.length).readAllBytes();
				} catch (IOException e) {
					logger.error("Error:", e);
				}
				return null;
			}));

			return audioFlux;
		} catch (IOException ie) {
			logger.error("File not found:", ie);
		} catch (Exception e) {
			logger.error("Error:", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("Error while closing file input stream:", e);
				}
			}
		}

		return Flux.empty();
	}
}
