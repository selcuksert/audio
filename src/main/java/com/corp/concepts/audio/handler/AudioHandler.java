package com.corp.concepts.audio.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.corp.concepts.audio.service.AudioConverterService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AudioHandler {

	private static final Logger logger = LoggerFactory.getLogger(AudioHandler.class);

	private AudioConverterService audioConverterService;

	public AudioHandler(AudioConverterService audioConverterService) {
		this.audioConverterService = audioConverterService;
	}

	public Mono<ServerResponse> audio(ServerRequest serverRequest) {
		float sampleRate = 48000;
		int sampleSizeInBits = 16;
		int channels = 2;
		try {

			Flux<byte[]> stream = audioConverterService.generateAudioStream("test.wav", sampleRate, sampleSizeInBits,
					channels);

			return ServerResponse.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
					.body(stream, byte[].class);
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Flux.empty(), byte[].class);
	}

}
