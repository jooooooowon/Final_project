<template>
  <div class="body-css">
  <div class="main">
    <div class="member">
    <div class="member-img">
      <img :src="profileImg()" alt="profile-img">
    </div>
    <div class="member-content">
      <div class="nickname">
        닉네임 : {{ memdto.nickname }}
      </div>
      <div class="winner-count">
        우승횟수 : {{ winCount }}
      </div>
    </div>
    </div>
    <div v-if="memdto.id == 'manager'" class = "menubar">
      <ul>
        <li>
          <span class="material-symbols-outlined" @click="[currentTab = 'changeTheme',clickedMehotd(0)]" :style=" {color : clicked[0] ? '#68a162' : '#2c3e50'}">
            change_circle
          </span>
        </li>
        <li>
          <span class="material-symbols-outlined" @click="[currentTab = 'randomCandidates',clickedMehotd(1)]" :style=" {color : clicked[1] ? '#68a162' : '#2c3e50'}">
            shuffle_on
          </span>
        </li>
        <li>
          <span class="material-symbols-outlined" @click="[currentTab = 'reportList',clickedMehotd(2)]" :style=" {color : clicked[2] ? '#68a162' : '#2c3e50'}">
          warning
          </span>
        </li>
        <li>
          <span class="material-symbols-outlined" @click="[currentTab = 'myPage',clickedMehotd(3)]" :style=" {color : clicked[3] ? '#68a162' : '#2c3e50'}">
            settings
          </span>
        </li>
      </ul>
      
    </div>
    <div v-else class="menubar">
      <ul>
        <li>
          <!-- 클릭하면 component를 변경해야 하므로 component의 이름을 책임지고 있는 currentTab의 이름을 변경해준다. -->
          <!-- 이 때 currentTab은 components에 선언해두었던 명과 같아야 실행된다. -->
          <span class="material-symbols-outlined" @click="[currentTab = 'myList',clickedMehotd(0)]" :style=" {color : clicked[0] ? '#68a162' : '#2c3e50'}">
            grid_view
            
          </span>
        </li>
        <li>
          <span class="material-symbols-outlined" @click="[currentTab = 'bookMark',clickedMehotd(1)]" :style=" {color : clicked[1] ? '#68a162' : '#2c3e50'}">
            bookmark
          </span>
        </li>
        <li>
          <span class="material-symbols-outlined" @click="[currentTab = 'myWinList',clickedMehotd(2)]" :style=" {color : clicked[2] ? '#68a162' : '#2c3e50'}">
            emoji_events
          </span>
        </li>
        <li>
          <span class="material-symbols-outlined" @click="[currentTab = 'myPage',clickedMehotd(3)]" :style=" {color : clicked[3] ? '#68a162' : '#2c3e50'}">
            settings
          </span>
        </li>
      </ul>
    </div>
    <!-- 여기에 나올 것들 -->
    <!-- currentTab이 곧 이용하기로 선언한 (components에 있는) 이름을 넣어준다. -->
    <!-- memnum = 은 이동한 component에서 이용할 props 엔간하면 memnum으로 설정하여 사용한다.  -->
    <!-- 이해가 잘되지 않는다면 myWinList.vue에서 사용되는 props를 보고 이용하자. -->
    <!-- 어찌보면 그냥 sessionStorage.getItem('memnum')으로 값을 이용할 수 있지만 -->
    <!-- 마이페이지에서 이동하는 것이 아닌, 다른 사람의 프로필을 보고싶을 때 이용할 수 있게 memnum을 props로 만들어 놓았다. -->
    <!-- 여러 방면으로 이용할 수 있겠다. -->
    <component :is="currentTab" :memnum = "memnum"></component>
  </div> 
</div>
  </template>
  
  <script>
  // 우선 이용할 컴포넌트들 import하고
  import changeTheme from '../battle/manager/changeTheme.vue'
  import reportList from '../community/ReportList.vue'
  import myPage from './Mypage.vue'
  import myWinList from './myWinList.vue'
  import randomCandidates from '../battle/manager/randomCandidates.vue'
  import bookMark from '../community/BookMark.vue'
  import myList from '../community/MyList.vue'
  
  export default{
    data(){
      return {
        currentTab : null,
        memnum : sessionStorage.getItem("memnum"),
        memdto : {
          memnum : 0,
          id : '',
          pwd : '',
          email : '',
          gender : '',
          nickname : '',
          img : ''
        },
        winCount : 0,
        // 그냥 클릭한 지 확인 용 (색깔 변경)
        clicked :[false, false, false, false]
      }
    },
    components:{
      // import한 components들 이용하겠다고 선언하고
      changeTheme : changeTheme,
      reportList : reportList,
      myPage : myPage,
      myWinList : myWinList,
      randomCandidates : randomCandidates,
      bookMark : bookMark,
      myList : myList
  
    },
    created:function(){
      let token = sessionStorage.getItem('token')
          
      const self = this;

      // 멤버 뽑기
      self.$axios.get(`http://localhost:7878/members/${self.memnum}`,
      {headers:{'token':token}})
      .then(function(res){
          if(res.status==200){
              let dto = res.data.dto
              if(dto!=null){
                self.memdto = dto;
                console.log(self.memdto)
                if(self.memdto.id == 'manager') {
                  self.currentTab = 'changeTheme'
                } else {
                  self.currentTab = 'myList'
                }
              }else{
                  alert('없는 아이디거나 만료된 토큰');
                  location.href = "/";
              }
          }else{
              alert('에러코드:'+self.status)
          }
      });
  
      // wincount 세기
      self.$axios.get(`http://localhost:7878/battles/winCount/${self.memnum}`)
      .then(res =>{
        if(res.status == 200){
          self.winCount = res.data.winCount;
        }
      })
    },
    methods:{
      profileImg(){
        let self = this;
        if(self.memdto.img != null){
            return 'http://localhost:7878/members/imgs/'+ self.memnum;
        } else{
            return require('@/assets/userImg.png')
        }
      },
      // 클릭 확인 용 true일 때만 색이 바뀐다.
      clickedMehotd(i){
        for(let idx = 0; idx < 4; idx++){
          if(idx == i){
            this.clicked[idx] = true;
          }else{
            this.clicked[idx] = false;
          }
        }
      }
    }
  }
  </script>
  
  <style scoped>
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
    /* text-align: center; */
}
  .material-symbols-outlined {
    /* font-variation-settings:
    'FILL' 1
    'wght' 400,
    'GRAD' 0,
    'opsz' 48, */
    cursor: pointer;
  }
  
  
  .main{
    width: 800px;
    left:0;
    right:0;
    margin:40px auto;
  }
  
  .member{
    display: flex;
    justify-content: space-evenly;
  }
  
  .member-img img {
  position: relative;
   width: 150px;
  /* 가로 사이즈 200px로 고정 */
   height: 150px;
   /* 세로 사이즈 200px로 고정 */
   object-fit: cover;
   /* 이미지가 카드 영역에 꽉 차도록 설정 */
   border-radius: 50%;
}
  
  .member-content{
    margin : 30px;
  }
  .member-content div{
    text-align: left;
    padding-bottom : 30px;
    font-size: 1.3em;
  }
  
  ul{
    display : flex;
    justify-content: center;
  }
  
  
  ul li{
    border:1px solid #000;
    padding:10px 70px;
  }
  
  span{
    transition: .5s;
  }
  </style>