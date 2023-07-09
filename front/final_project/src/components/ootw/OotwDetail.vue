<template>
    <div class="body-css">
        <h3> 나 뭐 입었더라?</h3>
        <span v-for="cloth in clothnum" :key="cloth">
            <span>
            <img :src="'http://localhost:8081/closets/img/' + memnum + '/' + cloth" alt="" style="border-radius: 15px;">
            {{ cloth }}
            </span>
        </span>
        <br /><br />
        <!-- 날짜 입력 -->
        <input type="text" v-model="odate" id="date" style="font-family: 'PyeongChang-Regular';
        font-size:13px; width:162px; height: 30px; text-align: center; color:black; font-weight: 500;
        border-color: lightgray; border-radius: 5px;" readonly><br /><br/>
        
        <!-- 날씨 입력 -->
        <input type="text" v-model="weather" id="weather" style="font-family: 'PyeongChang-Regular'; font-size:13.5px;
        width:160px; height: 30px; text-align: center; color:black; font-weight: 500; border-color: lightgray; border-radius: 5px;"
        readonly><br /><br/>
        
        <!-- 온도 입력 -->
        <input type="number" v-model="temp" id="temp" style="font-family: 'PyeongChang-Regular'; font-size:13.5px;
        width:161px; height: 30px; text-align: center; color:black; font-weight: 500; border-color: lightgray; border-radius: 5px;"
            readonly><br /><br/>
        
        <!-- 커멘트 입력 -->
        Comment<br />
        <textarea v-model="comments" cols="30" rows="5" style="resize:none; font-weight: 500; border-color: lightgray; border-radius: 5px;
        font-family: 'PyeongChang-Regular';" readonly></textarea><br />
        <button v-on:click="deleteOootw(ootwnum)">삭제</button>
    </div>
</template>

<script>
export default {
    name: 'OotwDetail',
    data() {
        return {
            ootwnum: this.$route.query.ootwnum,
            clothnum: [],
            memnum: '',
            odate: '',
            weather: '',
            temp: '',
            comments: ''
        }
    },
    created: function () {
        const self = this;
        self.memnum = sessionStorage.getItem('memnum')
        self.$axios.get('http://localhost:8081/boards/' + self.ootwnum)
            .then(function (res) {
                if (res.status == 200) {
                    let dto = res.data.dto;
                    let list = res.data.list;
                    self.odate = dto.odate
                    var year = dto.odate.substring(0, 4);
                    var month = dto.odate.substring(5, 7);
                    var day = dto.odate.substring(8, 10);
                    self.odate = year + "년 " + month + "월 " + day + "일";
                    self.weather = dto.weather
                    self.temp = dto.temp
                    self.comments = dto.comments
                    for (let i = 0; i < list.length; i++) {
                        self.clothnum.push(list[i].closetnum.closetnum);
                    }
                } else {
                    alert('에러코드: ' + res.status)
                }
            })
    },
    methods: {
        deleteOootw(ootwnum) {
            const self = this;
            self.$axios.delete('http://localhost:8081/boards/' + ootwnum)
                .then(function (res) {
                    if (res.status == 200) {
                        location.href = "/ootwlist";
                    } else {
                        alert('에러 코드: ' + res.status)
                    }
                })
        }
    }
}
</script>

<style scoped>
.body-css {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
}
h3 {
    margin: 40px 0 0;
}

img {
    width: 200px;
    height: 200px;
}

table {
    margin-left: auto;
    margin-right: auto;
}
</style>