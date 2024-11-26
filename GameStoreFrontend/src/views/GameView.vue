<template>
    <div class="game">
        <div class="header-container">
            <h1 id="title">Sid Meier's Civilization VI</h1>
            <span id="rating">
                Parental Rating: 
                14+
            </span> 
        </div>
        <div class ="game-overview">
            <img src="" alt="Civilization VI" width="300" height="400">
            <p>
                Civilization VI offers new ways to interact with your world, expand your empire across the map, advance your culture, and compete against history’s greatest leaders to build a civilization that will stand the test of time. Play as one of 20 historical leaders including Roosevelt (America) and Victoria (England).
            </p>
        </div>
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
    color: white;
    
    display: flex;
    flex-direction: column;

    margin-top: 0px;
    padding-top: 1rem;
    padding-left: 2rem;

    margin-left: 0%;
    margin-bottom: 30px;
    width: 100%;
    height: 100vh;

    border-radius: 10px;
}

.header-container {
    display: flex;
    align-items: center;           /* Vertically centers the items */
    justify-content: space-between; /* Places items on opposite ends */
}

#rating {
    font-size: 0.8em;  
    margin: 0px 2rem 0px 0px;         
}

#title {
    margin: 0;         /* Removes default margin */
}

</style>