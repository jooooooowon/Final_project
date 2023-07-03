<template>

  <div class="center">
    <section class="section1">
      <div class="container">
        <div class="title">Winner List</div>
        <div class="content">
            <span class="content-title">
              Show off your fashion!
            </span><br><br>
  
            Challenge others <br>
            to enter the Hall of Fame.<br>
            Show off your fashion!<br>
        </div>
      </div>
    </section>
    <section class="section2">
      <div class="container">
        <div class="title">Outfit Of The Weather!</div>
        <div class="content">
          <span class="content-title">
            Dress appropriately<br>
            for the weather!
          </span>
          <br><br>
          Remember the weather <br>
          and what to wear<br>
          on that special day.
  
        </div>
      </div>
    </section>
  </div>
  
  </template>

  <style scoped>
  @import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");
  
  html,
  body {
    width: 100%;
    height: 100%;
    background: #262626;
    font-family: sans-serif;
    overflow: hidden;
  }
  
  .center {
    position: relative;
    width: 100%;
    height: 100vh;
    margin: auto ;
    /* background-color: #616161; */
    overflow: hidden;
  }
  
  
    .section1 .container .title {
      padding-left:400px;
    }
    .container .title {
    position:absolute;
    font-family: "Pacifico", cursive;
    font-size: 5rem;
    color: #000;
    left:0;
    right:0;
    margin:100px auto;
    width: 500px;
    transition:1s;
  }
  .container .content { 
    position:absolute;
    left:30%;
    margin-top : 50px;
    font-size: 1em;
    font-family: "Pacifico", cursive;
    color: #000;
    line-height: 1.2;
    padding-left:50px;
    opacity:0;
    transition:1s;
    white-space: pre;
    text-align:left;
  }
  
  .content-title{
    font-size: 1.3em; 
    text-shadow: -1px 0 green, 0 1px green, 1px 0 green, 0 -1px green;
  }
  
  .section1 {
    position: absolute;
    top: 0;
    left: 0;
    right:0;
    margin:auto;
    width: 65%;
    height: 100%;
    padding: 50px 10px;
    /* background-color: red; */
    transform: skewX(20deg) translateX(-50%);
    overflow: hidden;
    transition:1s;
  }
  .section1:hover {
    padding: 50px;
    padding-left:300px;
    transform: skewX(20deg) translateX(-30%);
    z-index : 2;
  }
  .section1:hover .title {
    opacity:0;
  }
  
  .section1:hover .content {
    animation: skewBox 1s ease;
    opacity:1;
    font-size: 3em;
  }
  
  @keyframes skewBox{
    0%{
      left:0%;
    }
    100%{
      left:30%;
    }
  }
  .section1 .container {
    transform: skewX(-20deg);
    padding: 100px 0 0 85px;
  }
  
  .section1:before {
    content: '';
    width: 200%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    background: url(../assets/battle_index.png) no-repeat center;
    background-size: contain;
    transform: skewX(-20deg) translateX(-20%);
    z-index: -1;
  }
  
  .section2 {
    position: absolute;
    top: 0;
    left: 0;
    right:0;
    margin:auto;
    width: 65%;
    height: 100%;
    padding: 50px 10px;
    /* background-color: red; */
    transform: skewX(20deg) translateX(50%);
    overflow: hidden;
    transition:1s;
  }
  
  
  .section2 .container{
    transform: skewX(-20deg);
    padding: 100px 0 0 85px;
  }
  
  .section2:before {
    content: '';
    width: 200%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    background: url(http://source.unsplash.com/random/800x400) no-repeat center;
    background-size: contain;
    transform: skewX(-20deg) translateX(-25%);
    z-index: -1;
  }
  
  .section2:hover {
    padding: 50px;
    padding-right:150px;
    transform: skewX(20deg) translateX(30%);
  }
  
  .section2:hover .title {
    opacity:0;
  }
  
  .section2:hover .content {
    animation: section2 1s ease;
    opacity:1;
    font-size: 3em;
  }
  
  @keyframes section2{
    0%{
      left:100%;
    }
    100%{
      left:30%;
    }
  }
  
  
  </style>
  