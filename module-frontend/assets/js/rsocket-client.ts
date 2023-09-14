import { RSocketConnector } from "rsocket-core";
import { WebsocketClientTransport } from "rsocket-websocket-client";
import WebSocket from "isomorphic-ws";

// Sample
// https://github.com/rsocket/rsocket-js/blob/1.0.x-alpha/packages/rsocket-examples/src/ClientRequestChannelExample.ts
export class RSocket {
    private rsocket: any;

    public async connect() {
        const connector = new RSocketConnector({
            setup: {
                keepAlive: 100,
                lifetime: 10000,
            },
            transport: new WebsocketClientTransport({
                url: "ws://localhost:8080",
                wsCreator: (url) => new WebSocket(url) as any,
            }),
        });
        this.rsocket = await connector.connect();
        return this.rsocket;
    }

    public async requestChannel(contents: string) {
        if (!this.rsocket) await this.connect();

        return new Promise((resolve, reject) => {
            const requester = this.rsocket.requestChannel(
                {
                    data: Buffer.from(contents),
                },
                1,
                false,
                {
                    onError: (e: Error) => reject(e),
                    onNext: (payload: any, isComplete: boolean) => {
                        console.log(
                            `payload[data: ${payload.data}; metadata: ${payload.metadata}]|${isComplete}`
                        );

                        requester.request(1);

                        if (isComplete) {
                            resolve(payload);
                        }
                    },
                    onComplete: () => {
                        resolve(null);
                    },
                    onExtension: () => {},
                    request: (n: number) => {
                        console.log(`request(${n})`);
                        requester.onNext(
                            {
                                data: Buffer.from("Message"),
                            },
                            true
                        );
                    },
                    cancel: () => {},
                }
            );
        });
    }
}