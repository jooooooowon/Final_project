<template>
  <div class="body-css">
  <div v-if="prepared" style="display: flex;">
    
    <canvas id="canvas" style="position:absolute"></canvas>
    
    <div class="your-choice">
       <div style="font-size:35px">이번 주 배틀 테마는 [{{ firstCandidate.theme }}] 입니다!</div><br/>
       <div style="font-size:30px">{{ firstCandidate.theme }}을 더 잘 표현한 후보를 선택해 주세요.</div><br/>
       <div style="font-size:20px">※ 형평성을 위해 투표는 비밀투표로 진행됩니다.</div><br/>
    </div>
    <div class="main">
      <div class="first-candidate">
        <!-- {{ firstCandidate.batnum }} -->
        <span v-if="firstCandidate.batnum != selectedbatNum">
          <img :src="'http://localhost:7878/battles/imgs/'+firstCandidate.batnum" @click="voteCandidate(firstCandidate.batnum)" alt="첫번째 후보">
        </span>
        <span v-if="firstCandidate.batnum == selectedbatNum">
          <img :src="'http://localhost:7878/battles/imgs/'+firstCandidate.batnum" style="position: relative; z-index: 0;" alt="첫번째 후보">
          <img src="../../assets/selected.png" style="position:absolute; left:0; z-index: 1; opacity: 0.5;" alt="첫번째 후보">
        </span>
      </div>
      <div class="vs">
        VS
      </div>
      <div class="second-candidate">
        <span v-if="secondCandidate.batnum != selectedbatNum">
        <img :src="'http://localhost:7878/battles/imgs/'+secondCandidate.batnum" @click="voteCandidate(secondCandidate.batnum)" alt="두번째 후보">
        </span>
        <span v-if="secondCandidate.batnum == selectedbatNum">
          <img :src="'http://localhost:7878/battles/imgs/'+secondCandidate.batnum" alt="두번째 후보">
          <img src="../../assets/selected.png" style="position:absolute; left:0; z-index: 1; opacity: 0.5;" alt="두번째 후보">
        </span>
      </div>
    </div>
  </div>
  <div v-else>
    <notYet></notYet>
  </div>
</div>
</template>

<script>
import notYet from '@/components/battle/notYet.vue'

export default{
  name : 'battleVote',
  data(){
    return {
      dto:{},
      memnum : sessionStorage.getItem("memnum"),
      firstCandidate:{},
      secondCandidate:{},
      prepared : true,
      chk : true,
      selectedBatnumList: [],
      selectedbatNum: ''
    }
  },
  components:{
     notYet
  },
  created: function(){
    // 현재 로그인 상태 확인.
    let self = this;
    let token = sessionStorage.getItem('token');
    if(this.memnum == null){
      alert("로그인 후 사용 가능합니다.");
      location.href = "/"
    }else{

      self.$axios.get(`http://localhost:7878/members/${this.memnum}`,
      {headers:{'token':token}})
      .then(res =>{
        if(res.status == 200){
          this.dto = res.data.dto;
        }else{
          alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
        }
      })
      
      // 투표대상인 후보들 리스트 뽑기.
      self.$axios.get("http://localhost:7878/battles")
      .then(res =>{
        if(res.status == 200 ){
          if(res.data.len == 2){
            this.firstCandidate = res.data.list[0];
            this.secondCandidate = res.data.list[1];
            this.prepared = true;
          }
        }else{
          alert("오류 발생으로 인한 후보들 정보 불러오기 실패")
        }})

      // 투표 가능 유무 확인. (투표 한 번 하면 한 번더 못함.)
      self.$axios.get(`http://localhost:7878/votes/chk/${self.memnum}`)
      .then(res =>{
        if(res.status == 200){
          self.chk = res.data.chk;
          if(res.data.list == 0) {
            self.selectedbatNum = res.data.list;
          } else {
            self.selectedbatNum = res.data.list[0].batnum.batnum;
          }
        }else{
          alert("오류 발생으로 인한 투표 유무 확인 불가")
        }
      })

    }
        
  },
  mounted:function(){
    var c = document.getElementById("canvas");
      var ctx = c.getContext("2d");

      function resize() {
          var box = c.getBoundingClientRect();
          c.width = box.width;
          c.height = box.height;
      }

      var light = {
          x: 80,
          y: 100
      }

      var colors = ["#f5c156", "#e6616b", "#5cd3ad"];

      function drawLight() {
          ctx.beginPath();
          ctx.arc(light.x, light.y, 1000, 0, 2 * Math.PI);
          var gradient = ctx.createRadialGradient(light.x, light.y, 0, light.x, light.y, 500);
          gradient.addColorStop(0, "#3bccaa");
          gradient.addColorStop(1, "#96d9b1");
          ctx.fillStyle = gradient;
          ctx.fill();

          ctx.beginPath();
          ctx.arc(light.x, light.y, 5, 0, 2 * Math.PI);
          gradient = ctx.createRadialGradient(light.x, light.y, 0, light.x, light.y, 5);
          gradient.addColorStop(0, "#3bccaa");
          gradient.addColorStop(1, "#3b4654");
          ctx.fillStyle = gradient;
          ctx.fill();
      }

      function Box() {
          this.half_size = Math.floor((Math.random() * 15) + 1);
          this.x = Math.floor((Math.random() * c.width) + 1);
          this.y = Math.floor((Math.random() * c.height) + 1);
          this.r = Math.random() * Math.PI;
          this.shadow_length = 2000;
          this.color = colors[Math.floor((Math.random() * colors.length))];
        
          this.getDots = function() {

              var full = (Math.PI * 2) / 4;


              var p1 = {
                  x: this.x + this.half_size * Math.sin(this.r),
                  y: this.y + this.half_size * Math.cos(this.r)
              };
              var p2 = {
                  x: this.x + this.half_size * Math.sin(this.r + full),
                  y: this.y + this.half_size * Math.cos(this.r + full)
              };
              var p3 = {
                  x: this.x + this.half_size * Math.sin(this.r + full * 2),
                  y: this.y + this.half_size * Math.cos(this.r + full * 2)
              };
              var p4 = {
                  x: this.x + this.half_size * Math.sin(this.r + full * 3),
                  y: this.y + this.half_size * Math.cos(this.r + full * 3)
              };

              return {
                  p1: p1,
                  p2: p2,
                  p3: p3,
                  p4: p4
              };
          }
          this.rotate = function() {
              var speed = (60 - this.half_size) / 20;
              this.r += speed * 0.002;
              this.x += speed;
              this.y += speed;
          }
          this.draw = function() {
              var dots = this.getDots();
              ctx.beginPath();
              ctx.moveTo(dots.p1.x, dots.p1.y);
              ctx.lineTo(dots.p2.x, dots.p2.y);
              ctx.lineTo(dots.p3.x, dots.p3.y);
              ctx.lineTo(dots.p4.x, dots.p4.y);
              ctx.fillStyle = this.color;
              ctx.fill();


              if (this.y - this.half_size > c.height) {
                  this.y -= c.height + 100;
              }
              if (this.x - this.half_size > c.width) {
                  this.x -= c.width + 100;
              }
          }
          this.drawShadow = function() {
              var dots = this.getDots();
              var angles = [];
              var points = [];

              for (let dot in dots) {
                  var angle = Math.atan2(light.y - dots[dot].y, light.x - dots[dot].x);
                  var endX = dots[dot].x + this.shadow_length * Math.sin(-angle - Math.PI / 2);
                  var endY = dots[dot].y + this.shadow_length * Math.cos(-angle - Math.PI / 2);
                  angles.push(angle);
                  points.push({
                      endX: endX,
                      endY: endY,
                      startX: dots[dot].x,
                      startY: dots[dot].y
                  })
              }

              for (var i = points.length - 1; i >= 0; i--) {
                  var n = i == 3 ? 0 : i + 1;
                  ctx.beginPath();
                  ctx.moveTo(points[i].startX, points[i].startY);
                  ctx.lineTo(points[n].startX, points[n].startY);
                  ctx.lineTo(points[n].endX, points[n].endY);
                  ctx.lineTo(points[i].endX, points[i].endY);
                  ctx.fillStyle = "#96d9b1";
                  ctx.fill();
              }
          }
      }

      var boxes = [];

      function draw() {
          ctx.clearRect(0, 0, c.width, c.height);
          drawLight();

          for (let i = 0; i < boxes.length; i++) {
              boxes[i].rotate();
              boxes[i].drawShadow();
          }
          for (let i = 0; i < boxes.length; i++) {
              collisionDetection(i)
              boxes[i].draw();
          }
          requestAnimationFrame(draw);
      }

      resize();
      draw();

      while (boxes.length < 14) {
          boxes.push(new Box());
      }

      window.onresize = resize;
      c.onmousemove = function(e) {
          light.x = e.offsetX == undefined ? e.layerX : e.offsetX;
          light.y = e.offsetY == undefined ? e.layerY : e.offsetY;
      }


      function collisionDetection(b){
        for (var i = boxes.length - 1; i >= 0; i--) {
          if(i != b){	
            var dx = (boxes[b].x + boxes[b].half_size) - (boxes[i].x + boxes[i].half_size);
            var dy = (boxes[b].y + boxes[b].half_size) - (boxes[i].y + boxes[i].half_size);
            var d = Math.sqrt(dx * dx + dy * dy);
            if (d < boxes[b].half_size + boxes[i].half_size) {
              boxes[b].half_size = boxes[b].half_size > 1 ? boxes[b].half_size-=1 : 1;
              boxes[i].half_size = boxes[i].half_size > 1 ? boxes[i].half_size-=1 : 1;
            }
          }
        }
      }

  },
  methods:{
        
    // 투표 하기 버튼 클릭시 이벤트 리스너.
    voteCandidate(num){
      let self = this;
      if(!self.chk){
        alert('이미 투표 하였습니다.')
        location.href="/vote"
      }else{
        // alert(num);
        let form = new FormData();
        form.append("memnum",self.memnum);
        form.append("batnum",num);
        self.$axios.post('http://localhost:7878/votes',form)
        .then(res => {
          if(res.status == 200){
            // alert(res.data.batnum);
            location.reload();
            if(num == 1){
              this.firstCandidate = res.data.dto;
            }else if(num == 2){
              this.secondCandidate = res.data.dto;
            }

          }else{
            alert("오류 발생으로 인해 투표가 제대로 진행되지 않았습니다.");
          }
        })
      }
    }
  }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");
@font-face {
    font-family: 'PyeongChang-Regular';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
    font-weight: 400;
    font-style: normal;
}
.body-css {
    font-family: 'PyeongChang-Regular';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
}
html{
  height: 100%;
}
body{
		margin: 0;
		padding: 0;
		height: 100%;
		overflow: hidden;
		cursor: none;
}
#canvas{
		background-color: #96d9b1;
		width: 100%;
		height: 100%;		
}

.main{
  display:flex;
  position:absolute;
  width: 1000px;
  height: 500px;
  margin: 220px auto auto 270px;
  left:0;
  right:0;
}
.main img{
  width: 300px;
  height: 300px;
  /* margin-bottom:150px; */
}

.your-choice{
  font-family: 'PyeongChang-Regular';
  /* font-size: 3rem; */
  /* margin-top:50px; */
  z-index:1; 
  position:absolute;
  left:0; 
  right:0; 
  margin:50px auto;
}


.first-candidate{
  position:absolute;
  animation:first ease;
  animation-duration: 3s;
  left:10%;
  
}

.first-candidate img {
    width: 300px;
    /* 가로 사이즈 200px로 고정 */
    height: 450px;
    /* 세로 사이즈 200px로 고정 */
    object-fit: cover;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    border-radius: 10px;
}

.first-candidate:hover{
  cursor:pointer;
}

@keyframes first{
  0%{
    left:-100%;
  }
  70%{
    left:18%;
  }
  100%{
    left:10%;
  }
}

.second-candidate{
  position:absolute;
  animation:second ease;
  animation-duration: 3s;
  right:10%;
}

.second-candidate img {
    width: 300px;
    /* 가로 사이즈 200px로 고정 */
    height: 450px;
    /* 세로 사이즈 200px로 고정 */
    object-fit: cover;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    border-radius: 10px;
}
.second-candidate:hover{
  cursor:pointer;
}

@keyframes second{
  0%{
    right:-100%;
  }
  70%{
    right:18%;
  }
  100%{
    right:10%;
  }
}

.vs{
  display :absolute;
  font-family: 'PyeongChang-Regular';
  font-size: 1.8em;
  font-weight: bold;
  left:0;
  right:0;
  margin: 200px auto auto 480px;
  animation:vs-animate ease-in;
  animation-duration: 3s;
  z-index:1;
}

@keyframes vs-animate {
  0%{
    opacity:0;
  }
  100%{
    opacity : 1;
  }
}
</style>