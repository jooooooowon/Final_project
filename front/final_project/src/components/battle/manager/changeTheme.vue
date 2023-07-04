<template>
  <div>
    이번주의<br>
     주제는 {{ theme }}
  </div>
  <input type="text" v-model="theme">
  <input type="button" value="변경 하기" @click="changeTheme">

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

      self.$axios.put('http://localhost:8081/battles/manager',form)
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

div{
  font-family: 'Black Han Sans', sans-serif;
  font-size: 5rem;
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