<template>
    <div class="game">
        <h1>Game Display Page</h1>
        <p>Welcome to the Game Store!</p>
    </div>
</template>

<script>
export default {
    name: 'GameView',
}
</script>

<style scoped>
.game {
    text-align: center;
    background-color: #1e1e1e;
    
    display: flex;
    flex-direction: column;

    margin-top: 0px;
    padding-top: 1rem;

    margin-left: 0%;
    margin-bottom: 30px;
    width: 100%;
    height: 100vh;

    border-radius: 10%;
}
</style>