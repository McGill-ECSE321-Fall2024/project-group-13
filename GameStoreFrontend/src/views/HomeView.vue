<template>
    <section class="title">

        <!-- Title section  -->
        <div class="backgroundImage">
            <div class="titleFlex">
                <div class="titleText">
                    <div class="glitch-wrapper">
                        <div class="glitch" data-text="404 Games Not Found">404 Games Not Found</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Game Cards section  -->
        <div class="cardTitle">
                <h2>Featured & Recommended</h2>
            </div>
            
        <div class="cardFlex">
              
            <div class="card"><LpGameCard
                :image="roundsGame.img"
                :title="roundsGame.title"
                :price="roundsGame.price"
                :description="roundsGame.description" @click="handleGameClick(roundsGame)"/></div>

            <div class="card">
                <LpGameCard :image="r6Game.img"
                    :title="r6Game.title"
                    :price="r6Game.price"
                    :description="r6Game.description" @click="handleGameClick(r6Game)"/>
            </div>
            <div class="card">
                <LpGameCard :image="civ6Game.img"
                    :title="civ6Game.title"
                    :price="civ6Game.price"
                    :description="civ6Game.description" @click="handleGameClick(civ6Game)"/>
            </div>
        </div>

         <!-- Featured Promotions section  -->
         <div class="cardTitle">
                <h2>Promotions</h2>
         </div>
      
        <div class="promotionWrapper">
            <div class="promotionMain"></div>
            <div class="promotionDesc">
                <div class="promo1" @click="handleGameClick(altoGame)">
                    <div class="promosubtext">
                        <div class="promopct">
                            <span>-{{altoGame.promotionPercentage}}%</span>
                        </div>
                        <div class="promoprc">
                            <p class="oldprice">${{altoGame.price.toFixed(2)}}</p>
                            <p>${{(altoGame.price - (altoGame.price * altoGame.promotionPercentage / 100)).toFixed(2)}}</p>
                        </div>
                    </div>
                </div>
                <div class="promo2" @click="handleGameClick(d2Game)">
                    <div class="promosubtext">
                        <div class="promopct">
                            <span>-{{d2Game.promotionPercentage}}%</span>
                        </div>
                        <div class="promoprc">
                            <p class="oldprice">${{d2Game.price.toFixed(2)}}</p>
                            <p>${{(d2Game.price - (d2Game.price * d2Game.promotionPercentage / 100)).toFixed(2)}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

          <!-- Platform section  -->
          <div class="cardTitle">
                <h2>Our Platforms</h2>
        </div>
        <div class="platforms">     
                <img :src="xboxlogo" alt="xbox logo" class="platformLogo" />
                <img :src="ps5logo" alt="ps5 logo" class="platformLogo" />
                <img :src="switchlogo" alt="switch logo" class="platformLogo">
                <img :src="windowslogo" alt="windows logo" class="platformLogo">
                <img :src="wiilogo" alt="wii logo" class="platformLogo">
        </div>
    </section>
</template>

<script>
import LpGameCard from '@/components/LpGameCard.vue';
import xboxlogo from '../assets/xboxlogo.png';
import ps5logo from '../assets/ps5logo.png';
import switchlogo from '../assets/nintendologo.png';
import windowslogo from '../assets/windowslogo.png';
import wiilogo from '../assets/wiilogo.png';

import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'http://localhost:8080'
});

export default {
  name: 'HomeView',
  components: {
    LpGameCard
  },
  data() {
    return {
      xboxlogo,
      ps5logo,
      switchlogo,
      windowslogo,
      wiilogo,
      roundsGame: {},
        r6Game: {},
        civ6Game: {},
        altoGame: {},
        d2Game: {}
    };
  },

  async created() {
    // Fetch the displayed games by calling the API
    try {
        const [roundsResponse, r6Response, civ6Response, altoResponse, d2Response] = await Promise.all([
      axiosClient.get('/games', {params: {loggedInUsername: 'owner', title: 'Rounds'}}),
        axiosClient.get('/games', {params: {loggedInUsername: 'owner', title: 'Rainbow Six Siege'}}),
        axiosClient.get('/games', {params: {loggedInUsername: 'owner', title: 'Civilization VI'}}),
        axiosClient.get('/games', {params: {loggedInUsername: 'owner', title: 'Alto\'s Collection'}}),
        axiosClient.get('/games', {params: {loggedInUsername: 'owner', title: 'Destiny 2'}},)
    ]);

    this.roundsGame = roundsResponse.data.games[0];
    this.r6Game = r6Response.data.games[0];
    this.civ6Game = civ6Response.data.games[0];
    this.altoGame = altoResponse.data.games[0];
    this.d2Game = d2Response.data.games[0];

    } catch (error) {
        console.error(error);
    }
  },
  methods: {
    handleGameClick(game) {
      console.log('Game clicked: ', game); // later will route to game view
    }
  }
}
</script>

<style scoped>
    .promotionWrapper {
        margin-top: 20px;
        height: 550px;
        background-color: #121212;
        border-radius: 10px;
        margin-left: 85px;
        margin-right: 85px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.7);
        display: flex;
        flex-direction: row;
    }

    .promotionMain {
        flex: 2.45;
        border-top-left-radius: 10px;
        border-bottom-left-radius: 10px;
        background-image: url('../assets/promoBig.webp');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        display: flex;
        flex-direction: column;
    }

    .promotionDesc {
        flex: 1;
        border-top-right-radius: 10px;
        border-bottom-right-radius: 10px;

    }

    .promo1 {
        flex: 1;
        height: 50%;
        border-top-right-radius: 10px;
        background-image: url('../assets/alto.jpg');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        cursor: pointer;
    }

    .promo2 {
        flex: 1;
        height: 50%;
        border-bottom-right-radius: 10px;
        background-image: url('../assets/d2.jpg');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        cursor: pointer;
    }

    .promosubtext {
        display: flex;
        flex-direction: row;
        height: 15%;
        width: 30%;
        justify-content: space-between;
        align-items: center;
        font-size: small;
        gap: 8px;
        background: rgba(7, 226, 43, 0.1);
        backdrop-filter: blur(10px);
    }

    .promopct{
        padding-left: 5px;
    }

    .promopct>span {
        font-weight: bold;
        color: rgb(170, 228, 170);
        font-size: 1.5em;
        
    }

    .promoprc{
        display: flex;
        flex-direction: column;
        padding-right: 10%;
        color: rgb(209, 218, 226);
    }

    .oldprice {
        text-decoration: line-through;
        opacity: 0.7;
    }



    .cardTitle {
        margin-left: 85px;
        margin-top: 20px;
        margin-bottom: 0px;
        padding-bottom: 0px;
    }

    .backgroundImage {
        background-image: url('../assets/TitleBackground.jpg');
        background-size: cover;
        background-position: center center;
        background-repeat: no-repeat;
        height: 725px;
        width: 100%;
    }  

    .titleFlex {
        display: flex;
        height: 825px;
        align-items: center;
        justify-content: center;
    }

    .titleText>*{
        color: aliceblue;
        font-size: 5rem;
    }

    h1{
        font-weight: 500;
    }

    .titleText>h3{
    font-size: 1.5rem;
    font-weight: normal;
    margin-left: 5px;
    }

    .cardFlex {
    height: 500px; 
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-evenly;
    margin-left: 10px; 
    margin-right: 10px; 
}

    .card {
    height: 450px; 
    width: 400px; 
    background-color: black;
    margin: 10px;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

    .platforms {
        display: flex;
        height: 100px;
        align-items: center;
        justify-content: space-evenly;
    }

    .platformLogo{
        height: 30px;
    }

    /* Glitch title effect */
    .glitch-wrapper {
   width: 100%;
   height: 100%;
   display: flex;
   align-items: center;
   justify-content: center;
   text-align: center;
}

.glitch {
   position: relative;
   font-size: 90px;
   font-weight: bold;
   color: #FFFFFF;
   letter-spacing: 3px;
   z-index: 1;
}

.glitch:before,
.glitch:after {
   display: block;
   content: attr(data-text);
   position: absolute;
   top: 0;
   left: 0;
   opacity: 0.8;
}

.glitch:before {
   animation: glitch-it 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94) both infinite;
   color: #00FFFF;
   z-index: -1;
}

.glitch:after {
   animation: glitch-it 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94) reverse both infinite;
   color: #FF00FF;
   z-index: -2;
}

@keyframes glitch-it {
   0% {
      transform: translate(0);
   }
   20% {
      transform: translate(-2px, 2px);
   }
   40% {
      transform: translate(-2px, -2px);
   }
   60% {
      transform: translate(2px, 2px);
   }
   80% {
      transform: translate(2px, -2px);
   }
   to {
      transform: translate(0);
   }
}


</style>