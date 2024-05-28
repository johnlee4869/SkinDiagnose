<template>
    <div>
        <router-view v-slot="{Component}">
            <transition name="fade" mode="out-in" appear>
                <component :is="Component" />
            </transition>
        </router-view>
    </div>

</template>

<script setup>

</script>


<style lang="less" scoped>
    .fade-enter-active,
    .fade-leave-active {
        transition: opacity 0.8s ease;
    }


    .fade-enter-from,
    .fade-leave-to {
        opacity: 0;
    }
</style>
