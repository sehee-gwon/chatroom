<script setup lang="ts">
    import { RSocket } from '@/assets/js/rsocket-client';
    onMounted(async () => {
        try {
            const rsocket = new RSocket();
            await rsocket.connect();
        } catch (e) {
            console.log(e);
        }
    });
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
            <input type="text" placeholder="메시지를 입력하세요..." class="flex-1 p-4 border-r">
            <button class="px-6 py-2 text-white bg-blue-500 hover:bg-blue-600">전송</button>
        </div>
    </div>
</template>