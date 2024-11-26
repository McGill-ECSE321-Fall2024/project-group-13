<template>
    <div class="game">
        <div class="header-container">
            <h1 id="title">Sid Meier's Civilization VI</h1>
        </div>
        
        <div class="game-overview">
            <img src="../assets/civ6.jpg" alt="Civilization VI" width="480" height="270">
            <div id="game-info">
                <p id="descirption">
                    Civilization VI offers new ways to interact with your world, expand your empire across the map, advance your culture, and compete against history’s greatest leaders to build a civilization that will stand the test of time. Play as one of 20 historical leaders including Roosevelt (America) and Victoria (England).
                </p>
                <div class="rating-category">
                    <p id="game-rating">
                        Audience Rating:
                        4/5
                    </p>
                    <p id="category">
                        Game category:
                        Strategy
                    </p>

                    <p id="parental-rating">
                        Parental Rating: 
                        14+
                    </p>

                    <p id="stock">
                        Stock: 
                        10
                    </p>
                    
                </div>
            </div>
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
    padding: 1rem 1rem; 

    margin-left: 0%;
    margin-bottom: 30px;
    width: 100%;
    height: 100%;

    border-radius: 10px;
}

.header-container {
    display: flex;
    align-items: center;           
    justify-content: space-between; 

    margin-bottom: 1rem;
}

.game-overview {
    display: flex;

    img {
        border-radius: 5px;
    }
}

#game-info {
    display: flex;
    flex-direction: column;
    font-size: 1.2rem;
    
    margin-left: 20px; 

    #descirption{
        margin: 0;
        /* padding-bottom: 80px; */
    }

    .rating-category {
        display: flex;
        flex-direction: row;

        flex-wrap: wrap;
        align-content: center;

        gap: 1rem;
        
        justify-content: space-evenly;

        flex: 1; /*Takes up the remaining space */

        font-size: 0.8rem;  

        color: #619bda; /* Sets a different color */
        
        p {
            display: flex;
            justify-content: center;
            align-items: center;

            text-align: center;
            font-weight: bold;
            font-size: 0.9rem;
            color: rgb(255, 255, 255);

            border: solid 1px #c19aff;
            border-radius: 5px;
            width: 12rem;
            height: 1.8rem;

            background-color: #c19aff;
        }

        #category {
            /* margin-top: 1rem; */
            background-color: rgb(103, 159, 0);
            border-color: rgb(103, 159, 0);
        }

        #parental-rating {
            background-color: rgb(77, 0, 217);
            border-color: rgb(77, 0, 217);
        }

        #stock {
            background-color: rgb(17, 97, 255);
            border-color: rgb(17, 97, 255);
        }

        #game-rating {
            background-color: rgb(255, 0, 81);
            border-color: rgb(255, 0, 81);
        
        }
    }
}
</style>