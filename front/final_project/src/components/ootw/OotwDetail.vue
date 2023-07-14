<template>
    <div class="body-css">
        <br/><br/><br/><br/>
        <span v-for="closetNum in closetnumList" :key="closetNum">
            <span v-on:click="detailModal(closetNum)">
                <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + closetNum" alt=""
                    style="border-radius: 15px; cursor: pointer;" title="옷 정보 보기">
            </span>
        </span>
        <br /><br />
        <!-- 날짜 입력 -->
        <h3 style="font-family: 'PyeongChang-Regular';">날짜: {{ odate }}</h3>
        <!-- <input type="text" v-model="odate" id="date" style="font-family: 'PyeongChang-Regular';
        font-size:13px; width:162px; height: 30px; text-align: center; color:black; font-weight: 500;
        border-color: lightgray; border-radius: 5px;" readonly><br /><br /> -->

        <!-- 날씨 입력 -->
        <h3 style="font-family: 'PyeongChang-Regular';">날씨: {{ weather }}</h3>
        <!-- <input type="text" v-model="weather" id="weather"
            style="font-family: 'PyeongChang-Regular'; font-size:13.5px;
        width:160px; height: 30px; text-align: center; color:black; font-weight: 500; border-color: lightgray; border-radius: 5px;" readonly><br /><br /> -->

        <!-- 온도 입력 -->
        <h3 style="font-family: 'PyeongChang-Regular';">온도: {{ temp }}도</h3>
        <!-- <input type="number" v-model="temp" id="temp"
            style="font-family: 'PyeongChang-Regular'; font-size:13.5px;
        width:161px; height: 30px; text-align: center; color:black; font-weight: 500; border-color: lightgray; border-radius: 5px;" readonly><br /><br /> -->
        <br/>
        <!-- 커멘트 입력 -->
        Comment<br />
        <textarea v-model="comments" cols="30" rows="5" style="resize:none; font-weight: 500; border-color: lightgray; border-radius: 5px;
        font-family: 'PyeongChang-Regular';" readonly></textarea><br /><br />
        <button v-on:click="deleteOootw(ootwnum)">삭제</button><br /><br /><br />
    </div>

    <!-- 옷 디테일 모달창 -->
    <div class="modal-wrap-detail" v-show="modalCheckDetail" @click="modalCloseDetail" id="modalWrapDetail">
        <div class="modal-container-detail" @click.stop="" id="containerDetail">
            <label for="detailEditFile">
                <span v-if="img != 'basicImage' && detailEditImg == ''">
                    <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + setClosetnum" class="modal-img-detail">
                </span>
                <span v-if="img != 'basicImage' && detailEditImg != ''">
                    <img :src="detailEditImg" class="modal-img-detail">
                </span>
                <span v-if="img == 'basicImage' && detailEditImg == ''">
                    <img :src="'http://localhost:7878/closets/img/addimg/' + 0" class="modal-img-detail">
                </span>
                <span v-if="img == 'basicImage' && detailEditImg != ''">
                    <img :src="detailEditImg" class="modal-img-detail">
                </span>
            </label>
            <!-- <input type="file" id="detailEditFile" style="font-family: 'PyeongChang-Regular'; display: none"
                accept="image/*" @change="thumbnailChange"> -->
            <div class="modal-tags-detail">{{ maintag }} | {{ sub }}</div><br/>
            <div class="modal-search-detail">
                <!-- <input type="text" v-model="modalCloth" size="15" style="height: 38px; text-align: center;"> -->
            {{ modalCloth }}
            </div>
            <div class="modal-btn-detail"><button v-on:click="modalCloseDetail"
                    style="font-family: 'PyeongChang-Regular'; width:65px; height:38px;">닫기</button></div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'OotwDetail',
    data() {
        return {
            ootwnum: this.$route.query.ootwnum,
            closetnumList: [],
            memnum: '',
            odate: '',
            weather: '',
            temp: '',
            comments: '',
            // detail 모달
            setClosetnum: null,
            modalCloth: '',
            maintag: '',
            sub: '',
            img: null, // 기본이미지 유무 체크용
            modalCheckDetail: false,
            detailEditImg: '', // 디테일 이미지 수정 썸네일 이미지
            uploadimg: '', // 디테일 -> 이미지수정 -> 바뀐 이미지 주소 담는 변수
            thumbimg: ''
        }
    },
    created: function () {
        const self = this;
        self.memnum = sessionStorage.getItem('memnum')
        self.$axios.get('http://localhost:7878/boards/' + self.ootwnum)
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
                        self.closetnumList.push(list[i].closetnum.closetnum);
                    }
                } else {
                    alert('에러코드: ' + res.status)
                }
            })
    },
    methods: {
       // detail 모달창 열기
       detailModal(closetnum) {
            const self = this;
            this.setClosetnum = closetnum;
            self.modalCheckDetail = !self.modalCheckDetail;
            self.$axios.get('http://localhost:7878/closets/' + closetnum)
                .then(function (res) {
                    if (res.status == 200) {
                        let dto = res.data.dto
                        if (dto != null) {
                            self.modalCloth = dto.cloth
                            self.maintag = dto.maintag
                            self.sub = dto.subtag
                            self.img = dto.img
                        } else {
                            alert("정보가 없습니다.")
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        // detail 모달창 닫기
        modalCloseDetail() {
            this.modalCheckDetail = !this.modalCheckDetail;
        },
        deleteOootw(ootwnum) {
            const self = this;
            self.$axios.delete('http://localhost:7878/boards/' + ootwnum)
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

button {
    border: none;
    background-color: white;
    font-family: 'PyeongChang-Regular';
    border-radius: 5px;
    transition: .5s;
    font-size: 15px;
    font-weight: normal;
}

button:hover {
    /* background-color: #85b380; */
    color: #85b380;
    cursor: pointer;
    font-weight: bold;
}
/* ----- 옷 디테일 모달창 ----- */
.modal-wrap-detail {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    z-index: 2;
}

.modal-container-detail {
    overflow: auto;
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 290px;
    height: 58%;
    background: #fff;
    box-shadow: 0 20px 10px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    padding: 20px;
    box-sizing: border-box;
    /* 가운데정렬 */
    display: flex;
    flex-direction: column;
    /* 내용 수직으로 배치 */
    justify-content: center;
    /* 수직방향 가운데 정렬 */
    align-items: center;
    /* 수평방향 가운데 정렬 */
}

.modal-btn-detail {
    margin-bottom: -110px;
}
.modal-search-detail {
    margin-bottom: 10px;
}

.modal-img-detail {
    position: relative;
    width: 250px;
    /* 가로 사이즈 200px로 고정 */
    height: 250px;
    /* 세로 사이즈 200px로 고정 */
    object-fit: cover;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    border-radius: 10px;
    margin-top: -110px;
    margin-bottom: 20px;
}

.modal-tags-detail {
    margin-top: -5px;
    margin-bottom: 5px;
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