<template>
    <section class="browse">
        <div class="browseFlex">
            <!-- Title -->
            <div class="titleWrapper">
                <h1>Browse Products</h1>
            </div>

            <!-- Horizontal Bar -->
           <hr>

           <div class="leftGroup">

           </div>

             <!-- Search Bar -->
            <div class="searchWrapper">
                <input type="text" name="text" class="input" placeholder="Search for games by Title">
                <button class="search__btn">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="22" height="22">
                      <path d="M18.031 16.6168L22.3137 20.8995L20.8995 22.3137L16.6168 18.031C15.0769 19.263 13.124 20 11 20C6.032 20 2 15.968 2 11C2 6.032 6.032 2 11 2C15.968 2 20 6.032 20 11C20 13.124 19.263 15.0769 18.031 16.6168ZM16.0247 15.8748C17.2475 14.6146 18 12.8956 18 11C18 7.1325 14.8675 4 11 4C7.1325 4 4 7.1325 4 11C4 14.8675 7.1325 18 11 18C12.8956 18 14.6146 17.2475 15.8748 16.0247L16.0247 15.8748Z" fill="#efeff1"></path>
                    </svg>
                </button>
                <button id="clearBtn">Clear</button>
            </div>

            <!-- Game Cards -->
             <div class="gameCardWrapper">

             </div>


        </div>
    </section>
</template>

<script>
export default {
    name: 'BrowseView',
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
    margin: 10%;
    margin-top: 100px;
    margin-bottom: 30px;
    height: 87vh;
}

.titleWrapper {
    height: auto;
    height: 50px;
    width: 100%;
    text-align: left;
}

.titleWrapper>h1 {
    color: white;
    font-size: 30px;
}

hr {
    border: 0;
    height: 1px;
    background: #333;
    background-image: linear-gradient(to right, #ccc, purple, #ccc);
    margin-top: 10px;
}
/* Search Bar */

.searchWrapper {
    margin-top: 20px;
    height: 10%;
    width: 75%;
    background-color: #121212; 
    border-radius: 10px;
    display: flex;
    flex-direction: row;
    align-items: center;
    }

.input {
  height: 100%;
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
  transition: all .2s ease;
  height: 60%;
  width: 40%;
  margin-left: 8px;
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
  height: 100%;
  width: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60%;
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
/* On hover */
#clearBtn:hover {
    background-color: #a970ff;
    transform: scale(1.05); 
}
/* On click */
#clearBtn:active {
    background-color: #8c3de3; 
    transform: scale(0.98); 
}
/* On focus */
#clearBtn:focus {
    outline: none; 
    box-shadow: 0 0 0 3px rgba(147, 81, 247, 0.5); 
}

/* Game Cards / where the products are displayed */
.gameCardWrapper {
    background-color: #121212;
    width: 75%;
    height: 90%;
    margin-top: 20px;
    border-radius: 10px;
}

</style>