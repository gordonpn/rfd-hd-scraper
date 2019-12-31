<template id="app-frame">
    <div class="app-frame">
        <header>
            <span></span>
        </header>
        <slot></slot>
    </div>
</template>
<script>
    Vue.component("app-frame", {template: "#app-frame"});
</script>
<style>
    .app-frame > header {
        padding: 20px;
        background: #ae252a;
        font-size: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
</style>