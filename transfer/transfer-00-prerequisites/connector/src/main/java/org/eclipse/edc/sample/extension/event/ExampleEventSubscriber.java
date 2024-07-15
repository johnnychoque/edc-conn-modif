/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.edc.sample.extension.event;

import org.eclipse.edc.spi.event.Event;
import org.eclipse.edc.spi.event.EventEnvelope;
import org.eclipse.edc.spi.event.EventSubscriber;

public class ExampleEventSubscriber implements EventSubscriber {

    @Override
    public <E extends Event> void on(EventEnvelope<E> event) {
        // react to event
        var payload = event.getPayload();
        /*
        if (payload instanceof AssetCreated) {
            System.out.println("react only to AssetCreated events");
            System.out.println(payload);    
        }
        */
        
        System.out.println(">>>>>>>>>>>>>> EVENT >>>>>>>>>>>>>");
        System.out.println(payload.toString());
    }
}
