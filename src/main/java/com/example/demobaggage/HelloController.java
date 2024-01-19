/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demobaggage;

import java.util.Map;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tadaya Tsuyukubo
 */
@RestController
public class HelloController {

    private final ObservationRegistry registry;

    private final Tracer tracer;

    public HelloController(ObservationRegistry registry, Tracer tracer) {
        this.registry = registry;
        this.tracer = tracer;
    }

    @GetMapping("/hello")
    String hello(@RequestHeader Map<String, String> headers) {
        System.out.println("CONTROLLER: HEADERS=" + headers);
        System.out.println("CONTROLLER: ob=" + registry.getCurrentObservation());
        System.out.println("CONTROLLER: span=" + tracer.currentSpan());
        System.out.println("CONTROLLER: baggage=" + tracer.getBaggage("foo").get());
        return "Hello";
    }

}
