package com.example.demobaggage;

import java.util.List;

import brave.baggage.BaggageField;
import brave.baggage.BaggageFields;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.BaggageInScope;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveSpan;
import io.micrometer.tracing.brave.bridge.BraveSpanCustomizer;
import io.micrometer.tracing.brave.bridge.BraveTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class DemoBaggageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoBaggageApplication.class, "--debug");
    }

    @Bean
    CommandLineRunner runner2(Tracer tracer) {
        return (args) -> {
            Span span = tracer.nextSpan().name("parent").start();
            try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
                try (BaggageInScope baggage = tracer.createBaggageInScope(span.context(), "foo", "FOO")) {
                    System.out.println("AAA baggage=" + baggage.get());
                }
            }
            span.end();
        };
    }

    //    @Bean
    CommandLineRunner runner(RestTemplateBuilder builder, ObservationRegistry
            observationRegistry, Tracer tracer) {
        RestTemplate restTemplate = builder.build();
        return (args) -> {
//            Span span = tracer.nextSpan().name("parent").start();
            Span span = tracer.nextSpan().name("parent");
            System.out.println("AAA caller span = " + span);
//            try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
//                try (BaggageInScope scope = tracer.createBaggageInScope("foo", "ABCD")) {
//                    Observation observation = Observation.createNotStarted("foo", observationRegistry);
//                    observation.lowCardinalityKeyValue("abc", "ABC");
//                    observation.observe(() -> {
//                        System.out.println("AAA current-span=" + tracer.currentSpan());
//                        String result = restTemplate.getForObject("http://localhost:8080/hello", String.class);
//                        System.out.println("AAA received " + result);
//                        System.out.println("AAA received " + observation);
//                    });
//                }
//            }
            try (Tracer.SpanInScope ws = tracer.withSpan(span.start())) {
                System.out.println("AAA current-span1=" + tracer.currentSpan());
                try (BaggageInScope baggageInScope = tracer.createBaggageInScope(span.context(), "foo", "ABC333")) {
                    System.out.println("AAA baggageInScope=" + baggageInScope.get());
//                try (BaggageInScope baggageForExplicitSpan = tracer.createBaggageInScope(span.context(), "abc", "ABC333")) {
//                Observation observation = Observation.createNotStarted("foo", observationRegistry);
//                observation.lowCardinalityKeyValue("abc", "ABC");
//                observation.observe(() -> {
                    System.out.println("AAA current-span2=" + tracer.currentSpan());
                    String result = restTemplate.getForObject("http://localhost:8080/hello", String.class);
                    System.out.println("AAA received " + result);
//                    System.out.println("AAA received " + observation);
//                });
                }
            }
            span.end();
        };
    }

//    @Bean
//    BraveTracer braveTracerBridge(brave.Tracer tracer, CurrentTraceContext
//            currentTraceContext) {
//        return new BraveTracer(tracer, new BraveCurrentTraceContext(currentTraceContext), new BraveBaggageManager(List.of("abc")));
//    }

}
