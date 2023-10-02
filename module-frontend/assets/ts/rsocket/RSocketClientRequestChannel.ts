import {RSocket} from "rsocket-core";
import {ChatMessage} from 'assets/ts/ChatMessage';
import {Buffer} from "buffer";
import {RSocketClient} from "assets/ts/rsocket/RSocketClient";
import {Cancellable, OnExtensionSubscriber, OnNextSubscriber, OnTerminalSubscriber, Requestable} from "rsocket-core/dist/RSocket";

globalThis.Buffer = Buffer;

export class RSocketClientRequestChannel {
    private readonly rsocket?: RSocket;
    private requester?: OnTerminalSubscriber & OnNextSubscriber & OnExtensionSubscriber & Requestable & Cancellable;

    public constructor(rsocket: RSocket) {
        this.rsocket = rsocket;
    }

    public async requestChannel(number: number, chatMessage: ChatMessage): Promise<ChatMessage | undefined> {
        if (this.rsocket == null) throw new Error("RSocket is not connected.");

        return new Promise((resolve, reject) => {
            this.requester = this.rsocket?.requestChannel(
                {
                    data: Buffer.from(JSON.stringify(chatMessage)),
                    metadata: RSocketClient.createRoute('/chatRoom/' + number)
                },
                100,
                false,
                {
                    onError: (e: Error) => reject(e),
                    onNext: (payload: any, isComplete: boolean) => {
                        console.log(
                            `requestChannel: payload[data: ${payload.data} | metadata: ${payload.metadata}] | ${isComplete}`
                        );
                        if (isComplete) {
                            resolve(JSON.parse(payload.data));
                        }
                    },
                    onComplete: () => {
                        resolve(undefined);
                    },
                    onExtension: () => {},
                    request: (n: number) => {
                        console.log(`request(${n})`);
                    },
                    cancel: () => {},
                }
            );
        });
    }

    public request(chatMessage: ChatMessage) {
        if (this.requester == null) throw new Error("RSocket channel is not opened.");

        this.requester?.onNext(
            {
                data: Buffer.from(JSON.stringify(chatMessage))
            },
            false
        );
    }
}