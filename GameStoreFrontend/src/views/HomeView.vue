<template>
    <section class="title">
        <div class="backgroundImage">
            <div class="titleFlex">
                <div class="titleText">
                    <h1>404 Games Not Found</h1>
                </div>
            </div>
        </div>
        <div class="cardFlex">
            <div class="card"></div>
            <div class="card"></div>
            <div class="card"></div>
        </div>
    </section>
</template>

<script>
export default {
    name: 'HomeView',
}
</script>

<style scoped>

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
        height: 600px;
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

</style>