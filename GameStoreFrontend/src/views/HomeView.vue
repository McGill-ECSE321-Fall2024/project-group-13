<template>
    <section class="title">
        <div class="backgroundImage">
            <div class="titleFlex">
                <div class="titleText">
                    <h1>404 Games Not Found</h1>
                </div>
            </div>
        </div>

        <div class="cardTitle">
                <h2>Featured & Reccomended</h2>
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
            <div class="platformItem"></div>
            <div class="platformItem"></div>
            <div class="platformItem"></div>
            <div class="platformItem"></div>
            <div class="platformItem"></div>
        </div>
    </section>
</template>

<script>
import LpGameCard from '@/components/LpGameCard.vue';
import roundsImage from '../assets/rounds.jpg';
import r6Image from '../assets/r6.jpg';
import civ6Image from '../assets/civ6.jpg';

export default {
  name: 'HomeView',
  components: {
    LpGameCard
  },
  data() {
    return {
      roundsImage,
      r6Image,
      civ6Image
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
    
    .platformItem {
        background-color: aliceblue;
        width: 30px;
        height: 30px;
    }

</style>