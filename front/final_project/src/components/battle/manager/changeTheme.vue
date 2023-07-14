<template>
  <div class="body-css">
  <div>
    이번주의 주제는
    <br/>{{ theme }}
  </div>
  <input type="text" v-model="theme" style="font-family: 'PyeongChang-Regular';">
  <input type="button" value="변경 하기" @click="changeTheme" style="font-family: 'PyeongChang-Regular';">
</div>
</template>

<script>
export default{
  name:'changeTheme',
  data(){
    return{
      theme : null
    }
  },
  methods:{
    changeTheme : function(){
      let self = this;
      let form = new FormData();
      form.append('theme',this.theme);

      self.$axios.put('http://localhost:7878/battles/manager',form)
      .then(res => {
        if(res.status == 200){
          if(res.data.flag){
            alert("테마 변경 완료.");
            location.href="/"
          }
        }else{
          alert('오류로 인해 테마 변경 불가.')
        }
      })
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Black+Han+Sans&family=Diphylleia&display=swap');
@font-face {
  font-family: 'PyeongChang-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
  font-weight: normal;
  font-style: normal;
}
.body-css {
    font-family: 'PyeongChang-Regular';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
}
div{
  font-family: 'PyeongChang-Regular';
  font-size: 2rem;
  margin: 70px;
}

input[type="text"]{
  width: 250px;
  height: 30px;
  border-radius: 15px;
  padding-left:13px;
}

input[type="button"]{
  width: 100px;
  height: 40px;
  border-radius: 30px;
  background-color: #C4D7B2;
  transition : .5s;
  margin : 0 5px;
}

input[type="button"]:hover{
  background-color: #85b380;
  color: #ffffff;
  cursor:pointer;
  font-weight: bold;
}
</style>