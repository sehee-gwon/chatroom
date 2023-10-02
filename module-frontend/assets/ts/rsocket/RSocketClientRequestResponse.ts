import {RSocket} from "rsocket-core";
import {Buffer} from "buffer";
import {RSocketClient} from "assets/ts/rsocket/RSocketClient";
import {User} from "assets/ts/User";

globalThis.Buffer = Buffer;

export class RSocketClientRequestResponse {
    private readonly rsocket?: RSocket;

    public constructor(rsocket: RSocket) {
        this.rsocket = rsocket;
    }

    public async requestResponse(): Promise<User | undefined> {
        if (this.rsocket == null) throw new Error("RSocket is not connected.");

        return new Promise((resolve, reject) => {
            this.rsocket?.requestResponse(
                {
                    data: undefined,
                    metadata: RSocketClient.createRoute('/user/create')
                },
                {
                    onError: (e: Error) => reject(e),
                    onNext: (payload: any, isComplete: boolean) => {
                        console.log(
                            `requestResponse: payload[data: ${payload.data} | metadata: ${payload.metadata}] | ${isComplete}`
                        );
                        resolve(JSON.parse(payload.data));
                    },
                    onComplete: () => {
                        resolve(undefined);
                    },
                    onExtension: () => {}
                }
            );
        });
    }
}