<template>
    <section class="header-container">
            <h1 id="title">Sid Meier's Civilization VI</h1>
    </section>

    <section class="game-header">
        <div class="game-overview">
            <img src="../assets/civ6.jpg" alt="Civilization VI" width="480" height="270">
            <div id="game-info">
                <p id="descirption">
                    Civilization VI offers new ways to interact with your world, expand your empire across the map, advance your culture, and compete against history’s greatest leaders to build a civilization that will stand the test of time. Play as one of 20 historical leaders including Roosevelt (America) and Victoria (England).
                </p>
                <div class="rating-category">
                    <p id="game-rating">Audience Rating: 4/5</p>
                    <p id="category">Game Category: Strategy</p>
                    <p id="parental-rating">Parental Rating: 14+</p>
                    <p id="stock">Current Stock: 10</p>
                </div>

            </div>
        </div>
    </section>

    <section class="game-actions">
        <div class="purchasing-options">
            <div>
                <button id="buy-now">Add to cart</button><span id="price">10.49$ </span> <span id="promotion">-10%</span>
            </div>

            <div>
                <button id="add-wishlist">Add to your wishlist</button>
            </div> 
        </div>

        <!-- <div class="viewing-options">
            <button id="view-wishlist">View your wishlist</button>
        </div>  -->
    </section>

    
    <section class="review-section">
        <h1>Reviews</h1>

        <div class="reviews">
            <div class="review">
                <div class="review-header">
                    <p class="username">Mr. Fluffy</p>
                    <div class="likes-container">
                        <button class="like-button">Like</button>
                        <p class="likes">Likes: 10</p>
                    </div>
                </div>
                <p class="content">Great game, I love it!</p>
                <div class="review-footer">
                    <p class="rating">Rating: 5/5</p>
                    <p class="date">Date: 2021-10-10</p>
                </div>
            </div>
            <div class="review">
                <div class="review-header">
                    <p class="username">Anonymous</p>
                    <div class="likes-container">
                        <button class="like-button">Like</button>
                        <p class="likes">Likes: 7</p>
                    </div>
                </div>
                <p class="content">It's a good game, but it's not for everyone.</p>
                <div class="review-footer">
                    <p class="rating">Rating: 3/5</p>
                    <p class="date">Date: 2021-10-09</p>
                </div>
            </div>
            <div class="review">
                <div class="review-header">
                    <p class="username">Gamer123</p>
                    <div class="likes-container">
                        <button class="like-button">Like</button>
                        <p class="likes">Likes: 5</p>
                    </div>
                </div>
                <p class="content">It's a great game, but it's not for everyone.</p>
                <div class="review-footer">
                    <p class="rating">Rating: 4/5</p>
                    <p class="date">Date: 2021-10-08</p>
                </div>
            </div>
        </div>
    </section>

</template>

<script>
export default {
    name: 'GameView',
}
</script>

<style scoped>
.game-header {
    text-align: center;
    background-color: #1e1e1e;
    color: white;
    
    display: flex;
    flex-direction: column;

    padding: 1rem 1rem; 

    margin-left: 0%;
    margin-bottom: 20px;
    width: 100%;
    height: 100%;

    border-radius: 10px;
}

#title {
    font-size: 3rem;
    color: white;
    margin: 0;
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

            line-height: 1.8rem; /* Centers the botom two boxes' text */

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

.game-actions {
    display: flex;
    flex-direction: row;
    justify-content: space-between;

    margin-top: 20px;

    button {
        padding: 10px;
        border-radius: 5px;
        border: none;
        cursor: pointer;
        font-size: 1rem;
    }

    .purchasing-options {
        display: flex;
        flex-direction: row;

        gap: 10px;

        button {
            background-color: #619bda;
            color: white;
        }

        #buy-now {
            border-radius: 5px 0 0 5px;
            background-color: #7347ff;
            color: white;
        }

        #price {
            color: white;
            padding: 10px 10px 10px;
            border-radius: 0px 5px 5px 0px;
            border-bottom: 1px solid #7347ff;
            background-color: #997aff;
        }

        #promotion {
            padding: 10px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            font-size: 1rem;
            color: #5ba022;
        }

        #add-wishlist {
            background-color: #7347ff;
            color: white;
        }
    }

    #view-wishlist {
        background-color: #92c3e4;
        color: white;
    }
}

.review-section {
    font-size: 1.3rem;
    border-radius: 10px;
    padding: 10px;
    margin-top: 100px;
    background-color: #1e1e1e;

    h1 {
        color: white;
    }

    .reviews {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-top: 20px;

        .review {
            width: 100%;
            background-color: #696060;
            color: white;
            padding: 10px;
            border-radius: 5px;
            margin: 10px 0;

            .review-header {
                display: flex;
                justify-content: space-between;
                align-items: center;

                .username {
                    font-size: 1rem;
                    font-weight: bold;
                    text-decoration: underline;
                    margin: 0;
                }

                .likes-container {
                    display: flex;
                    align-items: center;

                    .like-button {
                        padding: 5px 10px;
                        margin-right: 10px;
                        border-radius: 5px;
                        border: none;
                        cursor: pointer;
                        background-color: #2f73f1;
                        color: white;
                        font-size: 0.9rem;
                    }

                    .likes {
                        margin: 0;
                        font-size: 0.9rem;
                    }
                }
            }

            .content {
                margin: 10px 0;
                font-size: 1rem;
            }

            .review-footer {
                display: flex;
                justify-content: space-between;
                align-items: center;

                .rating,
                .date {
                    font-size: 0.9rem;
                    margin: 0;
                }
            }
        }
    }
}


</style>