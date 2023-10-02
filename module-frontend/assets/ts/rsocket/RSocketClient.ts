import {RSocket, RSocketConnector} from 'rsocket-core';
import {WebsocketClientTransport} from 'rsocket-websocket-client';
import {encodeCompositeMetadata, encodeRoute, WellKnownMimeType} from 'rsocket-composite-metadata';
import WebSocket from 'isomorphic-ws';

import MESSAGE_RSOCKET_ROUTING = WellKnownMimeType.MESSAGE_RSOCKET_ROUTING;
import MESSAGE_RSOCKET_COMPOSITE_METADATA = WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA;

// Sample
// https://github.com/rsocket/rsocket-js/blob/1.0.x-alpha/packages/rsocket-examples/src/ClientRequestChannelExample.ts
export class RSocketClient {
    private rsocket?: RSocket;

    public async connect() {
        const connector = new RSocketConnector({
            setup: {
                keepAlive: 60000,
                lifetime: 180000,
                dataMimeType: 'application/json',
                metadataMimeType: MESSAGE_RSOCKET_COMPOSITE_METADATA.string
            },
            transport: new WebsocketClientTransport({
                url: 'ws://localhost:7000',
                wsCreator: (url) => new WebSocket(url) as any,
                debug: true
            }),
        });
        this.rsocket = await connector.connect();
        return this.rsocket;
    }

    public static createRoute(route: string) {
        let compositeMetaData = undefined;
        if (route) {
            const encodedRoute = encodeRoute(route);

            const map = new Map<WellKnownMimeType, Buffer>();
            map.set(MESSAGE_RSOCKET_ROUTING, encodedRoute);
            compositeMetaData = encodeCompositeMetadata(map);
        }
        return compositeMetaData;
    }
}