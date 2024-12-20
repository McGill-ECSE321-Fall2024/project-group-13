<template>
    <section class="browse">
        <div class="browseFlex">
            <!-- Title -->
            <div class="titleWrapper">
                <h1>Browse Products</h1>
                 <!-- Horizontal Bar -->
                <hr>    
            </div>

           

            <!-- Group Flex: Contains Left and Right Groups -->
            <div class="groupFlex">

                <!-- Left Group: Search Bar and Game Cards -->
                <div class="leftGroup">
                    <!-- Search Bar -->
                    <div class="searchWrapper">
                        <input type="text" name="text" class="input" placeholder="Search for games by Title" v-model="searchBar" v-on:keyup.enter="handleSearch">
                        <button class="search__btn" @click="handleSearch"> 
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="22" height="22">
                                <path d="M18.031 16.6168L22.3137 20.8995L20.8995 22.3137L16.6168 18.031C15.0769 19.263 13.124 20 11 20C6.032 20 2 15.968 2 11C2 6.032 6.032 2 11 2C15.968 2 20 6.032 20 11C20 13.124 19.263 15.0769 18.031 16.6168ZM16.0247 15.8748C17.2475 14.6146 18 12.8956 18 11C18 7.1325 14.8675 4 11 4C7.1325 4 4 7.1325 4 11C4 14.8675 7.1325 18 11 18C12.8956 18 14.6146 17.2475 15.8748 16.0247L16.0247 15.8748Z" fill="#efeff1"></path>
                            </svg>
                        </button>

                        <!-- Sort by Price Dropdown -->
                        <select 
                            class="sort-dropdown" 
                            v-model="sortPriceDirection" 
                            @change="handleSortByPrice"
                        >
                            <option disabled value="off">Sort by Price</option>
                            <option value="asc">Low to High</option>
                            <option value="desc">High to Low</option>
                        </select>

                         <!-- Sort by Promotion Dropdown -->
                         <select 
                            class="sort-dropdown" 
                            v-model="sortByPromos" 
                            @change="handleSortByPromos"
                        >
                            <option value="false">Show All Games</option>
                            <option value="true">Show Only Promotions</option>
                        </select>


                        <button id="clearBtn" @click="handleClear">Clear</button>
                    </div>

                    <!-- Game Cards -->
                    <div class="gameCardWrapper">
                        <!-- Game cards will be dynamically inserted here -->
                        <BrGameCard v-for="(game, index) in games" :key="index" :image="game.img" :title="game.title" 
                        :price="game.price" :description="game.description" :stock="game.stock" :promotionTitle="game.promotionName" 
                        :categoryId="game.categoryId" :gameId="game.gameID" :visibility="game.status" :promotionPercentage="game.promotionPercentage" 
                        :categoryName="game.categoryName" :rating="game.rating" @click="handleGameClick(game.gameID)"/>
                    </div>
                </div>

                <!-- Right Group: Category Filter -->
                <div class="rightGroup">
                    <h3>Search By Category</h3>
                    <hr style="background-image: none; padding: 1px; background-color: #333;">

                    <!-- Open Source Checkbox, credit: https://uiverse.io/Shoh2008/big-deer-80 -->
                    <svg style="display: none;">
                        <defs>
                            <filter id="goo-12">
                                <feGaussianBlur in="SourceGraphic" stdDeviation="4" result="blur"></feGaussianBlur>
                                <feColorMatrix in="blur" mode="matrix" values="
                                    1 0 0 0 0  
                                    0 1 0 0 0  
                                    0 0 1 0 0  
                                    0 0 0 22 -7" result="goo-12"></feColorMatrix>
                                <feBlend in="SourceGraphic" in2="goo-12"></feBlend>
                            </filter>
                        </defs>
                    </svg>

                    <!-- Dynamic Checkboxes -->
                    <div class="category-filters">
                        <div class="checkbox-wrapper-12" v-for="(category, index) in categories" :key="index">
                            <div class="cbx">
                                <input type="checkbox" :id="'cbx-12-' + index" v-model="selectedCategories" :value="category.name" @click="handleGetGamesByCategory(category.name, $event)">
                                <label :for="'cbx-12-' + index"></label>
                                <svg fill="none" viewBox="0 0 15 14" height="14" width="15">
                                    <path d="M2 8.36364L6.23077 12L13 2"></path>
                                </svg>
                            </div>
                            <label :for="'cbx-12-' + index" class="category-label">{{ category.name }}</label>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </section>
</template>

<script>
import BrGameCard from '@/components/BrGameCard.vue';

import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'http://localhost:8080'
});

export default {
    name: 'BrowseView',
    components: {
        BrGameCard
    },
    data() {
        return {
            categories: [],
            selectedCategories: [],
            games: [],
            searchBar: '',
            sortPriceDirection: 'off',
            sortByPromos: 'false'
        }
    },

    async created() {
        // Get the logged in username and permission level from the session storage
        console.log("Logged in username is: ", sessionStorage.getItem('loggedInUsername'));
        console.log("Permission level is: ", sessionStorage.getItem('permissionLevel'));

       try {
            // Fetch the games and the categories
            const [gameResponse, categoriesResponse] = await Promise.all([
                axiosClient.get('/games', { params: { loggedInUsername: sessionStorage.getItem('loggedInUsername') } }), 
                axiosClient.get('/categories', { params: { loggedInUsername: sessionStorage.getItem('loggedInUsername') } })
            ]);

            console.log(gameResponse.data, categoriesResponse.data); 

            this.games = gameResponse.data.games;
            this.categories = categoriesResponse.data.gameCategories;

       } catch (error) {
            console.error('Error fetching data:', error);
       }
    },

    methods: {
        async handleSearch() {
            try {
                this.selectedCategories = [];

                // All titles have first letter of each word capitalized
                let title = this.searchBar.toLowerCase().split(' ').map((s) => s.charAt(0).toUpperCase() + s.substring(1)).join(' ');

                const gameResponse = await axiosClient.get('/games', {params : { loggedInUsername: sessionStorage.getItem('loggedInUsername'), title: title}})

                console.log("Search bar response: ", gameResponse)

                this.games = gameResponse.data.games;

                // if the price sort is on, sort the games
                if (this.sortPriceDirection !== 'off') {
                    this.handleSortByPrice();
                }

                // if the promotions sort is on, sort the games
                if (this.sortByPromos === 'true') {
                    this.games = this.games.filter(game => game.promotionPercentage > 0);
                }

            } catch(error) {
                console.log("Error searching, " + error);
                this.games = [];
            }
        },

        async handleClear() {
        try {
            const gameResponse = await axiosClient.get('/games', {params : { loggedInUsername: sessionStorage.getItem('loggedInUsername')}});
            this.games = gameResponse.data.games;
            this.selectedCategories = [];
            this.searchBar = '';
            this.sortPriceDirection = 'off';
            this.sortByPromos = 'false';
        } catch(error) {
            console.log("Error clearing search, " + error);
            }},

        async handleGetGamesByCategory(category, event) {
            try{
                if (event.target.checked) {
                    this.selectedCategories = [category];
                    this.searchBar = '';
                    const gameResponse = await axiosClient.get('/games', {params : { loggedInUsername: sessionStorage.getItem('loggedInUsername'), category: category}});
                    this.games = gameResponse.data.games;
                    // if the price sort is on, sort the games
                    if (this.sortPriceDirection !== 'off') {
                        this.handleSortByPrice();
                    }
                    // if the promotions sort is on, sort the games
                    if (this.sortByPromos === 'true') {
                        this.games = this.games.filter(game => game.promotionPercentage > 0);
                    }
                } else {
                    this.selectedCategories = [];
                    const gameResponse = await axiosClient.get('/games', {params : { loggedInUsername: sessionStorage.getItem('loggedInUsername')}});
                    this.games = gameResponse.data.games;
                }


            } catch(error) {
                console.log("There was an error getting games by category: ", error);
                this.games = [];
            }
        },
        handleGameClick(gameId) {
            console.log("Game clicked: ", gameId); 
            // Navigate to the Game View based on the gameId
            this.$router.push({ name: 'Game', params: { gameID: gameId } });
        },
        handleSortByPrice() {
            console.log("Sort by price clicked");

                if (this.sortPriceDirection === 'asc') {
                     this.games.sort((a, b) => a.price - b.price);
                } else if (this.sortPriceDirection === 'desc') {
                      this.games.sort((a, b) => b.price - a.price);
            }
},
        handleSortByPromos() {
            console.log("Sort by promotions clicked");

            if (this.sortByPromos === 'true') {
                this.games = this.games.filter(game => game.promotionPercentage > 0);
            } else {
                this.handleClear();
            }
        }
    },
}
</script>

<style scoped>
.browse {
    text-align: center;
    margin-top: 50px;
}

.browseFlex {
    display: flex;
    flex-direction: column;
    margin: 5%;
    margin-top: 70px;
    margin-bottom: 30px;
    height: calc(100vh - 150px); 
}

.titleWrapper {
    height: 50px;
    width: 100%;
    text-align: left;
}

.titleWrapper > h1 {
    color: white;
    font-size: 30px;
}

hr {
    border: 0;
    height: 1px;
    background: #333;
    background-image: linear-gradient(to right, #272525, purple, #272525);
    margin-top: 10px;
}

/* Group Flex: Container for Left and Right Groups */
.groupFlex {
    display: flex;
    flex-direction: row;
    height: 100%;
    width: 100%;
    margin-top: 20px; 
    align-items: stretch; 
}

/* Left Group: Contains Search Bar and Game Cards */
.leftGroup {
    flex: 4.0;
    display: flex;
    flex-direction: column; 
    margin-right: 20px; 
}

/* Search Wrapper */
.searchWrapper {
    width: 100%; 
    background-color:  #1e1e1e; 
    border-radius: 10px;
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 10px;
    margin-bottom: 10px; 
}

.input {
    width: 40%;
    outline: none;
    font-size: 14px;
    font-weight: 500;
    background-color: #313134;
    caret-color: #f7f7f8;
    color: #fff;
    padding: 7px 10px;
    border: 2px solid transparent;
    border-top-left-radius: 7px;
    border-bottom-left-radius: 7px;
    margin-right: 1px;
    margin-left: 8px;
    transition: all .2s ease;
}

.input:hover {
    border: 2px solid rgba(255, 255, 255, 0.16);
}

.input:focus {
    border: 2px solid #a970ff;
    background-color: #0e0e10;
}

.search__btn {
    border: none;
    cursor: pointer;
    background-color: rgba(42, 42, 45, 1);
    border-top-right-radius: 7px;
    border-bottom-right-radius: 7px;
    height: 90%;
    width: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: background-color 0.2s ease;
}

.search__btn:hover {
    background-color: rgba(54, 54, 56, 1);
}

#clearBtn {
    background-color: #9351f7;
    color: #ffffff; 
    border: none;
    border-radius: 8px; 
    padding: 10px 20px; 
    margin: 0 8px; 
    margin-left: auto;
    cursor: pointer;
    font-size: 16px; 
    transition: background-color 0.3s ease, transform 0.2s ease; 
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 100px; 
}

/* Button States */
#clearBtn:hover {
    background-color: #a970ff;
    transform: scale(1.05); 
}

#clearBtn:active {
    background-color: #8c3de3; 
    transform: scale(0.98); 
}

#clearBtn:focus {
    outline: none; 
    box-shadow: 0 0 0 3px rgba(147, 81, 247, 0.5); 
}

/* Game Cards */
.gameCardWrapper {
    background-color: #1e1e1e;
    width: 100%; 
    flex-grow: 1; 
    margin-top: 0; 
    border-radius: 10px;
    overflow-y: auto; 
    padding: 10px; 

    /* Setup 4x... grid */
    display: grid;
    grid-template-columns: repeat(4, 225px); 
    justify-content: center; 
    gap : 30px; 
    
    max-height: 575px;
}

.rightGroup {
    flex: 1; 
    background-color: #1e1e1e;
    color: #fff;
    padding: 20px;
    border-radius: 10px;
    display: flex; 
    flex-direction: column;
    overflow-y: auto;
    max-height: 645px;
}

/* Category Filters Container */
.category-filters {
    display: flex;
    flex-direction: column;
    margin-top: 20px;
}

/* Checkbox Wrapper */
.checkbox-wrapper-12 {
    position: relative;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
}

.checkbox-wrapper-12 > svg {
    position: absolute;
    top: -130%;
    left: -170%;
    width: 110px;
    pointer-events: none;
}

.checkbox-wrapper-12 * {
    box-sizing: border-box;
}

.checkbox-wrapper-12 input[type="checkbox"] {
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
    -webkit-tap-highlight-color: transparent;
    cursor: pointer;
    margin: 0;
}

.checkbox-wrapper-12 input[type="checkbox"]:focus {
    outline: 0;
}

.checkbox-wrapper-12 .cbx {
    width: 24px;
    height: 24px;
    position: relative;
}

.checkbox-wrapper-12 .cbx input {
    position: absolute;
    top: 0;
    left: 0;
    width: 24px;
    height: 24px;
    border: 2px solid #bfbfc0;
    border-radius: 50%;
    background: none;
    z-index: 2;
}

.checkbox-wrapper-12 .cbx label {
    width: 24px;
    height: 24px;
    background: none;
    border-radius: 50%;
    position: absolute;
    top: 0;
    left: 0;
    transform: translate3d(0, 0, 0);
    pointer-events: none;
    filter: url(#goo-12);
}

.checkbox-wrapper-12 .cbx svg {
    position: absolute;
    top: 5px;
    left: 4px;
    z-index: 1;
    pointer-events: none;
}

.checkbox-wrapper-12 .cbx svg path {
    stroke: #fff;
    stroke-width: 3;
    stroke-linecap: round;
    stroke-linejoin: round;
    stroke-dasharray: 19;
    stroke-dashoffset: 19;
    transition: stroke-dashoffset 0.3s ease;
    transition-delay: 0.2s;
}

.checkbox-wrapper-12 .cbx input:checked + label {
    animation: splash-12 0.6s ease forwards;
}

.checkbox-wrapper-12 .cbx input:checked + label + svg path {
    stroke-dashoffset: 0;
}

.category-label {
    margin-left: 40px;
    font-size: 16px;
    color: #ffffff;
    cursor: pointer;
}

/* Animation Keyframes */
@keyframes splash-12 {
    40% {
        background: #866efb;
        box-shadow: 0 -18px 0 -8px #866efb, 16px -8px 0 -8px #866efb, 16px 8px 0 -8px #866efb, 0 18px 0 -8px #866efb, -16px 8px 0 -8px #866efb, -16px -8px 0 -8px #866efb;
    }
    100% {
        background: #866efb;
        box-shadow: 0 -36px 0 -10px transparent, 32px -16px 0 -10px transparent, 32px 16px 0 -10px transparent, 0 36px 0 -10px transparent, -32px 16px 0 -10px transparent, -32px -16px 0 -10px transparent;
    }
}

/* Adjust the scroll behavior of rightGroup if there are many categories */
.rightGroup {
    overflow-y: auto;
}

/* Adjust the max height to ensure scroll works */
.browseFlex {
    height: calc(100vh - 150px); 
}

/* Make scrollbar thinner */
.gameCardWrapper::-webkit-scrollbar,
.rightGroup::-webkit-scrollbar {
    width: 8px;
}

.gameCardWrapper::-webkit-scrollbar-track,
.rightGroup::-webkit-scrollbar-track {
    background: #1e1e1e;
}

.gameCardWrapper::-webkit-scrollbar-thumb,
.rightGroup::-webkit-scrollbar-thumb {
    background-color: #555;
    border-radius: 4px;
    border: 2px solid #1e1e1e;
}

.gameCardWrapper::-webkit-scrollbar-thumb:hover,
.rightGroup::-webkit-scrollbar-thumb:hover {
    background-color: #888;
}

.gameCardWrapper,
.rightGroup {
    scrollbar-width: thin;
    scrollbar-color: #555 #1e1e1e;
}

/* Sort Dropdown */
.sort-dropdown {
    background-color: #313134;
    color: #fff;
    border: none;
    border-radius: 8px;
    padding: 10px 15px;
    margin-left: 10px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.sort-dropdown:hover {
    background-color: #4a4a4a;
}

.sort-dropdown:focus {
    outline: none;
    box-shadow: 0 0 0 3px rgba(147, 81, 247, 0.5);
}


</style>

