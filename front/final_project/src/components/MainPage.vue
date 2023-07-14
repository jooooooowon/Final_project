<template>

  <div class="center">
    <section class="section1" @click="$router.push('/weather')" style="cursor: pointer;">
      <div class="container">
        <div class="title">WEATHER</div> 
        <div class="content">
            <!-- <span class="content-title">
              기온별 옷차림
            </span><br><br> -->
            <img src="@/assets/closet1.png" style="width:350px;height:auto;">
            
            <br/><br/>
            오늘의 날씨를 확인하고<br>
            기온에 맞는 옷장 속 아이템을 추천받고<br>
            비슷한 날씨에 입었던 옷차림을 확인하세요!
        </div>
      </div>
    </section>
    <section class="section2" @click="$router.push('/winnerList')" style="cursor: pointer;">
      <div class="container">
        <div class="title">BATTLE</div>
        <div class="content">
          <!-- <span class="content-title">
            Dress appropriately<br>
            for the weather!
          </span> -->
          <img src="@/assets/battle.png" style="width:350px;height:auto;">
          <br><br>
          테마별 코디로 배틀에 도전하세요!<br>
          랜덤으로 선정된 두 도전자의 코디 중 <br>
          테마와 가장 알맞는 코디에 투표하세요!
  
        </div>
      </div>
    </section>
  </div>
  
  </template>

  <style scoped>
  @import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");
  @font-face {
    font-family: 'PyeongChangPeace-Bold';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChangPeace-Bold.woff2') format('woff2');
    font-weight: 700;
    font-style: normal;
  }
  @font-face {
  font-family: 'PyeongChang-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
  font-weight: 400;
  font-style: normal;
  }
  @font-face {
  font-family: "Flood Std"; 
  src: url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.eot"); 
  src: url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.eot?#iefix") format("embedded-opentype"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.woff2") format("woff2"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.woff") format("woff"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.ttf") format("truetype"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.svg#Flood Std") format("svg"); }

  
  /* html,
  body {
    width: 100%;
    height: 100%;
    background: #262626;
    font-family: sans-serif;
    overflow: hidden;
  } */
  
  .center {
    position: relative;
    width: 100%;
    height: 100vh;
    margin: auto ;
    /* background-color: #616161; */
    overflow: hidden;
  }
  
  
    .section1 .container .title {
      margin-top: 200px;
      padding-left:400px;
      /* color: #2c3e50; */
      color:white;
      font-size: 100px;
      /* text-shadow: 0 0 1px #fff, 0 0 20px #fff, 0 0 30px #fff, 0 0 40px rgb(255, 255, 160), 0 0 70px rgb(255, 255, 160), 0 0 80px rgb(255, 255, 160), 0 0 100px rgb(255, 255, 160), 0 0 150px rgb(255, 255, 160); */
    }

    .section2 .container .title {
      /* color: white; */
      margin-top: 200px;
      color:black;
      font-family: "Flood Std"; 
      font-size: 120px;
      text-shadow: 0 0 4px #ccc, 0 -5px 4px #ff3, 2px -10px 6px #fd3, -2px -15px 11px #f80, 2px -19px 18px #f20;
    }
    .container .title {
    position:absolute;
    /* font-family: "Pacifico", cursive; */
    font-family: "Flood Std"; 
    font-size: 5rem;
    /* color: #000; */
    left:0;
    right:0;
    margin:100px auto;
    width: 500px;
    transition:1s;
    text-align: center;
  }
  .container .content { 
    position:absolute;
    left:30%;
    margin-top : 10px;
    font-size: 1em;
    /* font-family: "Pacifico", cursive; */
    font-family: 'PyeongChang-Regular';
    /* color: #2c3e50; */
    color: white;
    line-height: 1.2;
    /* padding-left:50px; */
    opacity:0;
    transition:1s;
    white-space: pre;
    /* text-align:left; */
  }
  
  /* .content-title{
    font-size: 1.3em; 
    text-shadow: -1px 0 green, 0 1px green, 1px 0 green, 0 -1px green;
  } */
  
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
    text-align: left;
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
    font-size: 25px;
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
    /* padding-left:50px; */
  }
  
  .section1:before {
    content: '';
    width: 200%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    /* background-image: linear-gradient(white, #4dc4ff); */
    background: url(../assets/catSky.gif) no-repeat center;
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
    text-align: right;
  }
  
  
  .section2 .container{
    transform: skewX(-20deg);
    padding: 100px 0 0 85px;
    /* padding-left:100px; */
  }
  
  .section2:before {
    content: '';
    width: 200%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0px;
    /* background-image: linear-gradient(orange, rgb(244, 63, 2)); */
    background: url(../assets/fire.gif) no-repeat center;
    /* background: url(http://source.unsplash.com/random/800x400) no-repeat center; */
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
    /* margin-top : 10px; */
    animation: section2 1s ease;
    opacity:1;
    font-size: 25px;
    right:50px;
    /* color: white; */
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
  