/*
 *  Copyright (c) 2024 Universidad de Cantabria (UC)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Johnny Choque (UC)
 *
 */

package org.eclipse.edc.sample.extension.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.eclipse.edc.http.spi.EdcHttpClient;
import org.eclipse.edc.spi.event.Event;
import org.eclipse.edc.spi.event.EventEnvelope;
import org.eclipse.edc.spi.event.EventSubscriber;

import java.io.IOException;
import java.util.Map;

/**
 * Event subscriber that will send the Agreement ID to the Provider App when the agreement has been reached.
 */
public class AgreementReached implements EventSubscriber {
    private final EdcHttpClient httpClient;

    AgreementReached(EdcHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public <E extends Event> void on(EventEnvelope<E> event) {
        var payload = event.getPayload();
        /*
        if (payload instanceof AssetCreated) {
            System.out.println("react only to AssetCreated events");
            System.out.println(payload);    
        }
        */
        
        System.out.println(">>>>>>>>>>>>>> EVENT >>>>>>>>>>>>>");
        System.out.println(payload.toString());
        sendTestMessage();
    }

    private void sendTestMessage() {
        final MediaType jsonSetting = MediaType.get("application/json; charset=utf-8");
        final String url = "http://localhost:3000/api/todos";
        
        final ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonBody = mapper.writeValueAsString(Map.of("access_token", "token"));
            RequestBody body = RequestBody.create(jsonBody, jsonSetting);

            var request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            var response = httpClient.execute(request);
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}