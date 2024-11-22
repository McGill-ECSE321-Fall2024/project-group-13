<template>
    <section class="title">
        <div class="backgroundImage">
            <div class="titleFlex">
                <div class="titleText">
                    <div class="glitch-wrapper">
                        <div class="glitch" data-text="404 Games Not Found">404 Games Not Found</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="cardTitle">
                <h2>Featured & Recommended</h2>
            </div>
            
        <div class="cardFlex">
              
            <div class="card"><LpGameCard
                :image="roundsImage"
                title="Rounds"
                price="$19.99"
                description="Rounds is a 1v1 rogue-lite card game"/></div>

            <div class="card">
                <LpGameCard :image="r6Image"
                    title="Rainbow Six Siege"
                    price="$24.99"
                    description="Tom Clancy's Rainbow Six® Siege is a team-based shooter"/>
            </div>
            <div class="card">
                <LpGameCard :image="civ6Image"
                    title="Civilization VI"
                    price="$14.99"
                    description="Expand your empire and be history’s greatest leader"/>
            </div>
        </div>

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

        <div class="promotion-wrapper">

        </div>
    </section>
</template>

<script>
import LpGameCard from '@/components/LpGameCard.vue';
import roundsImage from '../assets/rounds.jpg';
import r6Image from '../assets/r6.jpg';
import civ6Image from '../assets/civ6.jpg';
import xboxlogo from '../assets/xboxlogo.png';
import ps5logo from '../assets/ps5logo.png';
import switchlogo from '../assets/nintendologo.png';
import windowslogo from '../assets/windowslogo.png';
import wiilogo from '../assets/wiilogo.png';

export default {
  name: 'HomeView',
  components: {
    LpGameCard
  },
  data() {
    return {
      roundsImage,
      r6Image,
      civ6Image,
      xboxlogo,
      ps5logo,
      switchlogo,
      windowslogo,
      wiilogo
    };
  }
}
</script>

<style scoped>
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
        height: 825px;
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

    .cardFlex{
        height: 550px;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-evenly;
    } 

    .card{
        height: 500px;
        width: 450px;
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