<script setup lang="ts">
    import {reactive} from 'vue';
    import {RSocketClient} from 'assets/ts/rsocket/RSocketClient';
    import {RSocketClientRequestChannel} from 'assets/ts/rsocket/RSocketClientRequestChannel';
    import {RSocketClientRequestResponse} from 'assets/ts/rsocket/RSocketClientRequestResponse';
    import {ChatMessage} from 'assets/ts/ChatMessage';

    const client = new RSocketClient();
    const connector = await client.connect();

    const response = new RSocketClientRequestResponse(connector);
    const channel = new RSocketClientRequestChannel(connector);

    const chatMessage = reactive(new ChatMessage());

    onMounted(async () => {
        try {
            chatMessage.user = await response.requestResponse();
            await channel.requestChannel(1, chatMessage);
        } catch (e) {
            console.error(e);
        }
    })

    const sendMessage = async () => {
        channel.request(chatMessage);
        chatMessage.message = '';
    }
</script>

<template>
    <div class="flex flex-col h-screen p-4 bg-gray-200">
        <div class="flex-1 overflow-y-auto mb-4 bg-white rounded shadow">
<!--            <div class="p-4 border-b">
                <p class="text-sm text-gray-500">사용자명1</p>
                <p>안녕하세요! 채팅 테스트 중입니다.</p>
            </div>-->
        </div>

        <div class="flex items-center bg-white rounded shadow">
            <input type="text" class="flex-1 p-4 border-r" placeholder="메시지를 입력하세요..." v-model="chatMessage.message">
            <button class="px-6 py-2 text-white bg-blue-500 hover:bg-blue-600" @click="sendMessage()">전송</button>
        </div>
    </div>
</template>