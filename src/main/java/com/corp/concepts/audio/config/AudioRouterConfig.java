package com.corp.concepts.audio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.corp.concepts.audio.handler.AudioHandler;

@Configuration
public class AudioRouterConfig {

	@Bean
	public RouterFunction<ServerResponse> route(AudioHandler audioHandler) {
		return RouterFunctions.route(RequestPredicates.GET("/audio"), audioHandler::audio);
	}
}
