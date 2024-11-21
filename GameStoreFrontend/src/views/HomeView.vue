<template>
    <section class="title">
        <div class="backgroundImage">
            <div class="titleFlex">
                <div class="titleText">
                    <h1>404 Games Not Found</h1>
                </div>
            </div>
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

</style>