<template>
    <div id="up1"></div>
    <!-- <h3>옷장 전체리스트</h3> -->
    <div>
        <input type="search" v-model="cloth" size="30" style="height: 38px; text-align: center;" placeholder="옷 검색하기">
        <span class="searchBtn"><b-button v-on:click="clothserach" style="width:65px; height:40px;">검색</b-button></span>
        <div class="addCloth"><b-button href="/closetadd" style="width:150px; height:40px;" class="addBtn">내옷
                등록하기</b-button></div>
        <div class="menu-wrapper">
            <div class="menu-bar">
                <div v-for="(item, index) in menuItems" :key="index" class="menu-item">
                    <div class="main-tag" @mousedown="toggleSubmenu(index)" v-on:click="getall(index)">{{ item.title }}
                    </div>
                    <div class="sub-menu" :class="{ active: item.isOpen }">
                        <div class="sub-items-container">
                            <div v-for="(subtag, subIndex) in item.subItems" :key="subIndex" class="sub-item"
                                v-on:click="listbytag(subtag, subIndex)">
                                {{ subtag }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div v-show="memnum == checkMemnum">
            <div class="container" v-for="(row, index) in additionalCloset" :key="index"
                style="display: flex; align-items: center;">
                <div class="card" v-for="closet in row" :key="closet.closetnum">
                    <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + closet.closetnum">
                    <div class="favImg">
                        <a v-on:click="favorite(closet.closetnum)">
                            <img :src="closet.favorite === 1 ? image1 : image2" style="width:40px; height:40px">
                        </a>
                    </div>
                    <div class="inform">
                        <span class="word-spacing">{{ closet.maintag }} {{ closet.subtag }}</span>
                        <br />
                        <a v-on:click="modalOpen(closet.closetnum)">{{ closet.cloth }}</a><br />
                        <!-- v-on:click="detail(closet.closetnum)" -->
                    </div>
                    <div class="btn-container">
                        <b-button v-on:click="deletecloth(closet.closetnum, closet.favorite)">삭제</b-button>
                    </div>
                </div>
            </div>
        </div><br />
        <div v-show="closetlist == ''">등록된 옷이 없습니다.</div>
        <span class="more-btn"><b-button v-on:click="moreBtn" style="width:80px;">더보기</b-button></span>

        <!-- 옷 디테일 모달창 -->
        <div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
            <div class="modal-container" @click.stop="" id="container">
                <label for="imgtag">
                    <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + setClosetnum" id="thumbimg"
                        class="modal-img">
                </label>
                <input type="file" id="imgtag" style="display: none" accept="image/*" v-on:change="thumbnail($event)">
                <div class="modal-tags">{{ maintag }} | {{ sub }}</div>
                <div class="modal-search"><input type="search" v-model="modalCloth" size="15"
                        style="height: 38px; text-align: center;"></div>
                <div class="modal-btn"><b-button v-on:click="change(setClosetnum)">수정</b-button></div>
            </div>
        </div>
        <!-- <div v-show="memnum == checkMemnum">
            <b-card-group deck v-for="(row, index) in additionalCloset" :key="index"
            style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
            <b-card v-for="closet in row" :key="closet.closetnum">
                    <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + closet.closetnum"
                        style="width:200px; height:200px;">
                        <div class="favImg">
                            <a v-on:click="favorite(closet.closetnum)">
                                <img :src="closet.favorite === 1 ? image1 : image2" style="width:40px; height:40px">
                            </a>
                        </div>
                        <div class="inform">
                            <b-card-text>
                            <span class="word-spacing">{{ closet.maintag }} {{ closet.subtag }}</span>
                            <br />
                            <a v-on:click="detail(closet.closetnum)">{{ closet.cloth }}</a><br />
                        </b-card-text>
                    </div>
                    <template #footer>
                        <small class="text-muted">
                            <b-button v-on:click="deletecloth(closet.closetnum, closet.favorite)">삭제</b-button>
                        </small>
                    </template>
                </b-card>
            </b-card-group>
        </div><br /> -->
    </div>
    <div class="goupBtn"><b-button href="#up1" class="moveBtn" style="width:80px;">Go up</b-button></div>
    <br />
</template>

<script>
export default {
    name: 'ClosetlistByTag',
    data() {
        return {
            image1: 'http://localhost:7878/closets/img/addimg/' + 2,
            image2: 'http://localhost:7878/closets/img/addimg/' + 1,
            tag: this.$route.query.tag,
            index: this.$route.query.index,
            closetlist: [],
            additionalCloset: [],
            closetPerPage: 4,
            currentPage: 1,
            memnum: sessionStorage.getItem('memnum'),
            cloth: '',
            checkMemnum: '',
            // 모달
            setClosetnum: null,
            modalCloth: '',
            maintag: '',
            sub: '',
            modalCheck: false,
            uploadimg: '', // 디테일 -> 이미지수정 -> 바뀐 이미지 주소 담는 변수
            thumbimg: '',
            menuItems: [
                { title: "전체", isOpen: false, subItems: [] },
                { title: "아우터", isOpen: false, subItems: ['아우터(전체)', '가디건', '자켓', '야상', '트렌치코트', '코트', '패딩', 'etc'] },
                { title: "상의", isOpen: false, subItems: ['상의(전체)', '민소매', '반팔', '긴팔티', '셔츠', '니트', '맨투맨', 'etc'] },
                { title: "하의", isOpen: false, subItems: ['하의(전체)', '반바지', '치마', '면바지', '청바지', '레깅스', 'etc'] },
                { title: "기타", isOpen: false, subItems: ['기타(전체)', '스타킹', '히트텍', '기모제품', '방한용품', 'etc'] },
                { title: "신발", isOpen: false, subItems: ['신발(전체)', '샌들', '슬리퍼', '운동화', '등산화', '구두', 'etc'] }
            ]
        }
    },
    created: function () { // 해당 컴포넌트가 처음 실행될 때만 적용... 그 다음부터는 변경된 컴포넌트(같은 컴포넌트로 이동할 때 적용이 안됨)
        const self = this;
        self.memnum = sessionStorage.getItem('memnum')
        if (self.index == 0) {
            let maintag = self.$route.query.tag.split('(', 1);
            self.$axios.get('http://localhost:7878/closets/maintags/' + maintag)
                .then(function (res) {
                    if (res.status == 200) {
                        self.closetlist = res.data.list;
                        if (self.closetlist != '') {
                            self.checkMemnum = self.closetlist[0].memnum.memnum;
                        }
                        const addtionalRow1 = self.closetlist.slice(0, self.closetPerPage);
                        const addtionalRow2 = self.closetlist.slice(self.closetPerPage, self.closetPerPage * 2);
                        self.additionalCloset.push(addtionalRow1, addtionalRow2);
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        } else {
            let subtag = this.$route.query.tag;
            self.$axios.get('http://localhost:7878/closets/subtags/' + subtag)
                .then(function (res) {
                    if (res.status == 200) {
                        // 컴포넌트 처음 로딩될 때 옷장에서 999999999번 default 걸러서 리스트에 넣기
                        self.closetlist = res.data.list;
                        if (self.closetlist != '') {
                            self.checkMemnum = self.closetlist[0].memnum.memnum;
                        }
                        const addtionalRow1 = self.closetlist.slice(0, self.closetPerPage);
                        const addtionalRow2 = self.closetlist.slice(self.closetPerPage, self.closetPerPage * 2);
                        self.additionalCloset.push(addtionalRow1, addtionalRow2);
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        }
    },
    methods: {
        toggleSubmenu(index) {
            if (index > 0) {
                for (let i = 0; i < this.menuItems.length; i++) {
                    if (i !== index) {
                        this.menuItems[i].isOpen = false; // 다른 메인태그의 서브메뉴를 닫음
                    }
                }
                this.menuItems[index].isOpen = !this.menuItems[index].isOpen; // 현재 메인태그의 서브메뉴를 토글
            }
        },
        getall(index) {
            // const self = this;
            if (index == 0) { // 메인태그 '전체'일 때만 전체 리스트 보여주기 설정
                location.href = '/closetlist'
                // self.$axios.get('http://localhost:7878/closets')
                //     .then(function (res) {
                //         if (res.status == 200) {
                //             self.closetlist = res.data.list.filter(closet => closet.closetnum != 999999999);
                //             self.displayedcloset = self.closetlist.slice(0, self.closetPerPage);
                //         } else {
                //             alert('에러코드: ' + res.status)
                //         }
                //     })
            }
        },
        moreBtn() {
            const self = this;
            // 처음에 3개(0~3..0, 1, 2)를 보여준다면 더보기 버튼을 눌렀을 때 3개가 추가되어 0~6(0, 1, 2, 3, 4, 5)이 되어야 함
            // 추가할 시작 번호 = 현재페이지 * 보여주는 리스트 개수 = 1 * 3 = 3
            // 추가할 끝날 번호 = 추가할 시작 번호 + 보여주는 리스트 개수 = 3 + 3 = 6
            // 처음 보여지는 배열에 원래 배열의 3~6을 slice해서 껴넣는다.. => then, 0~6
            // 그리고 현재페이지++ 시켜서 위 과정을 반복할 수 있게 한다.
            // ex) more = 현재페이지(2) * 보여주는 리스트 개수(3) = 2 * 3 = 6
            // end = more(6) + 보여주는 리스트 개수(3) = 9
            // 0~6 + 6~9 = 0~9 .. 0, 1, 2, 3, 4, 5, 6, 7, 8.. 9개가 보여짐
            const startIndex = (self.currentPage) * self.closetPerPage*2;
            const endIndex = startIndex + self.closetPerPage;
            if (startIndex > self.closetlist.length) {
                // 더 이상 표시할 아이템이 없으면 더보기 버튼을 비활성화
                return;
            }
            const addtionalRow = self.closetlist.slice(startIndex, endIndex)
            self.additionalCloset.push(addtionalRow);
            // self.displayedcloset = [...self.displayedcloset, ...self.closetlist.slice(startIndex, endIndex)];
            self.currentPage++;
        },
        detail(closetnum) {
            this.$router.push({ name: 'ClosetDetail', query: { closetnum: closetnum } })
        },
        deletecloth(closetnum) {
            const self = this;
            self.$axios.delete('http://localhost:7878/closets/' + closetnum)
                .then(function (res) {
                    if (res.status == 200) {
                        if (res.data.flag) {
                            location.reload();
                            // self.additionalCloset = self.additionalCloset.map(row => row.filter(closet => closet.closetnum != closetnum));
                            // self.additionalCloset = self.additionalCloset.filter(closet => closet.closetnum != closetnum);
                            // filter() method: 자바스크립트의 배열 method..
                            // 주어진 배열(self.closetlist)을 method 내부에 있는 조건에 만족하는 열들을 새로운 배열로 생성하는 method
                            // 기존에 있는 배열의 요소 closet 객체의 closetnum 속성들을 모두 꺼내서 입력된 변수 closetnum과 비교한다.
                            // 입력된 변수와 기존 배열 속성이 같지 않을 때(true) 그 속성들로만 배열을 다시 생성한다.
                            // 즉 삭제를 위해 입력된 변수 closetnum이 기존 배열 속성 closetnum과 일치하므로 해당 열은 새로운 배열에 속할 수 없다. 
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        favorite(closetnum) {
            const self = this;
            self.$axios.patch('http://localhost:7878/closets/' + closetnum)
                .then(function (res) {
                    if (res.status == 200) {
                        const updatedCloset = self.closetlist.find(closet => closet.closetnum == closetnum);
                        // find() method: 자바스크립트의 배열 method
                        // 주어진 배열(self.closetlist)에서 내부 조건에 만족하는 열을 찾는 method
                        // 즐겨찾기 같은 경우는 좋아요 갱신에 만족하는 하나의 열을 찾아야 하므로 find() method를 사용함
                        // filter() method를 사용하면 조건에 만족하는 하나의 열만 새로운 배열로 생성돼서 보여줌
                        // 무튼, 조건에 만족하는 열을 찾아서 상수 updatedCloset에 저장한다.
                        // 그리고 아래의 if문에서 해당 상수가 null이 아닐 경우,
                        // 요소 updatedCloset 객체의 favorite 속성을 조건부 연산자를 사용하여 업데이트한다.
                        // 조건부 연산자(삼항연산자): 조건식에 만족하면 true인 0 값을, 만족하지 않으면 false인 1 값을 준다. 0과 1은 아래 예시.
                        // span tag에서 1일 때는 ★, 0일 때는 ☆로 표시가 되도록 조건부 연산자를 지정해 놨었는데,
                        // 내가 이미 좋아요가 된(favorite이 1) 게시글을 다시 누르면 back단 method에 의해 favorite이 0으로 바뀌고(서버 따로),
                        // front에서는 아래의 조건부 연산자에 의해 기존에 favorite이 1이었으므로 0으로 바꿔서 별 모양을 0에 맞는 ☆로 자동으로 변환(앞단 따로)
                        if (updatedCloset) {
                            updatedCloset.favorite = updatedCloset.favorite == 1 ? 0 : 1;
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        selectsub(index) {
            const self = this;
            if (index == 0) {
                self.subtags = []
            } else if (index == 1) {
                self.subtags = ['아우터(전체)', '가디건', '자켓', '야상', '트렌치코트', '코트', '패딩', 'etc']
            } else if (index == 2) {
                self.subtags = ['상의(전체)', '민소매', '반팔', '긴팔티', '셔츠', '니트', '맨투맨', 'etc']
            } else if (index == 3) {
                self.subtags = ['하의(전체)', '반바지', '치마', '면바지', '청바지', '레깅스', 'etc']
            } else if (index == 4) {
                self.subtags = ['기타(전체)', '스타킹', '히트텍', '기모제품', '방한용품', 'etc']
            } else if (index == 5) {
                self.subtags = ['신발(전체)', '샌들', '슬리퍼', '운동화', '등산화', '구두', 'etc']
            }
        },
        listbytag(subtag, index) {
            const self = this;
            self.$router.push({ name: 'ClosetListByTag2', query: { tag: subtag, index: index } });
        },
        clothserach() {
            const self = this;
            if (self.cloth == '') {
                alert('검색할 옷을 입력해주세요.');
            } else {
                self.$router.push({ name: 'ClosetListByCloth2', query: { cloth: self.cloth } });
            }
        },
        // 카드 누르면 옷 디테일 페이지로 넘어감
        modalOpen(closetnum) {
            const self = this;
            this.setClosetnum = closetnum;
            self.modalCheck = !self.modalCheck;
            self.$axios.get('http://localhost:7878/closets/' + closetnum)
                .then(function (res) {
                    if (res.status == 200) {
                        let dto = res.data.dto
                        if (dto != null) {
                            self.modalCloth = dto.cloth
                            self.maintag = dto.maintag
                            self.sub = dto.subtag
                        } else {
                            alert("정보가 없습니다.")
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        // 모달창 닫기
        modalClose() {
            this.modalCheck = !this.modalCheck;
        },

        // 옷 디테일 모달에서 옷 수정하기
        change(closetnum) {
            const self = this;
            let formdata = new FormData();
            if (self.uploadimg == null) {
                self.$axios.patch('http://localhost:7878/closets/editcloth/' + closetnum + "/" + self.cloth)
                    .then(function (res) {
                        if (res.status == 200) {
                            let newdto = res.data.dto
                            self.cloth = newdto.cloth;
                            location.reload();
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            } else {
                formdata.append('newf', self.uploadimg)
                self.$axios.patch('http://localhost:7878/closets/editcloth/' + closetnum + "/" + self.cloth, formdata)
                    .then(function (res) {
                        if (res.status == 200) {
                            let newdto = res.data.dto
                            self.cloth = newdto.cloth;
                            location.reload();
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        },
        thumbnail(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => {
                    console.log(reader.result);
                    document.querySelector('#thumbimg').src = reader.result;
                    console.log(document.querySelector('#thumbimg'));
                    console.log(document.querySelector('#thumbimg').src);
                    //this.thumb = reader.result;
                    this.uploadimg = file;
                };
            }
        }
    }
}
</script>

<style scoped>
h3 {
    margin: 40px 0 0;
}

ul {
    margin-left: 27%;
    cursor: pointer;
}

li {
    list-style: none;
    float: left;
    margin-right: 80px;
    cursor: pointer;
}

.follow {
    clear: left;
}

.container {
    display: flex;
    /* flex-wrap: wrap;
    justify-content: center; */
    gap: 45px;
    margin-top: 20px;
    margin-left: 330px;
}

.card {
    width: 225px;
    height: 345px;
    background-color: white;
    border-radius: 10px;
    padding: 10px;
    text-align: center;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
}

.card img {
    position: relative;
    width: 200px;
    /* 가로 사이즈 200px로 고정 */
    height: 200px;
    /* 세로 사이즈 200px로 고정 */
    object-fit: cover;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    border-radius: 10px;
}

.favImg {
    margin-left: 165px;
    /* margin-top: 150px; */
    cursor: pointer;
}

a {
    cursor: pointer;
}

.inform {
    /* margin-left: 150px; */
    margin-top: -20px;
}

.word-spacing {
    word-spacing: 5px;
}

button {
    background-color: rgba(38, 37, 37, 0.5);
    border: none;
}

.more-btn {
    /* margin-left: 205px; */
    position: fixed;
    bottom: 60px;
    right: 20px;
}

.moveBtn {
    background-color: rgba(38, 37, 37, 0.5);
    border: none;
}

.addBtn {
    background-color: rgba(34, 183, 34, 0.5);
    border: none;
}

.addCloth {
    margin-left: 1080px;
    margin-top: -40px;
}

.searchBtn {
    margin-left: 5px;
}

.btn-container {
    display: flex;
    flex-wrap: wrap;
    /* 필요한 경우 버튼이 줄바꿈되도록 함 */
    justify-content: space-between;
    border-top: 1px solid lightgray;
    width: 120px;

}

.btn-container button {
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
    flex: 1 0 45%;
    /* 버튼의 크기 조정 */
    margin: 5px;
    /* 버튼 사이의 간격 설정 */
    background-color: rgba(18, 76, 18, 0.5);
    border: none;
}

.goupBtn {
    position: fixed;
    bottom: 20px;
    right: 20px;
}

.modal-wrap {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.1);
}


/* modal or popup */
.modal-container {
    overflow: auto;
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 310px;
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

.modal-btn {
    margin-bottom: -110px;
}

.modal-search {
    margin-bottom: 10px;
}

.modal-img {
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
    cursor: pointer;
}

.modal-tags {
    margin-top: -5px;
    margin-bottom: 5px;
}

/* ----- 메뉴바 ----- */

.menu-wrapper {
    position: fixed;
    top: 270px;
    left: 40px;
    width: 17%;
    z-index: 100;
    box-shadow: 0 10px 10px rgba(0, 0, 0, 0.1);
}

/* .menu-wrapper: 메뉴 바를 감싸는 컨테이너에 적용되는 클래스 선택자입니다.
position: fixed: 요소를 페이지에서 고정 위치로 설정합니다.
top: 0: 컨테이너를 페이지의 상단에 위치시킵니다.
left: 0: 컨테이너를 페이지의 왼쪽에 위치시킵니다.
width: 20%: 컨테이너의 너비를 부모 요소의 너비의 20%로 설정합니다.
z-index: 100: 컨테이너의 층위를 설정하여 다른 요소 위에 나타나도록 합니다. */
.menu-bar {
    background-color: white;
    border: 1px solid lightgray;
    padding: 5px;
}

/* .menu-bar: 메뉴 바 전체에 적용되는 클래스 선택자입니다.
width: 20%: 메뉴 바의 너비를 부모 요소의 너비의 20%로 설정합니다.
background-color: lightgray: 메뉴 바의 배경색을 연한 회색으로 설정합니다.
border: 1px solid lightgray: 메뉴 바의 테두리를 1픽셀 두께의 연한 회색 실선으로 설정합니다.
padding: 5px: 메뉴 바의 안쪽 여백을 5픽셀로 설정합니다. */

.menu-item {
    margin-bottom: 10px;
}

/* .menu-item: 각 메뉴 항목에 적용되는 클래스 선택자입니다.
margin-bottom: 10px: 메뉴 항목 아래쪽에 10픽셀의 여백을 추가합니다. */

.main-tag {
    cursor: pointer;
}

/* .main-tag: 메뉴 항목의 메인 태그에 적용되는 클래스 선택자입니다.
cursor: pointer: 마우스 커서를 가리킬 때 포인터 모양으로 변경합니다. */

.sub-menu {
    height: 0;
    overflow: hidden;
    transition: height 0.5s ease;
    /* border-bottom: 1px solid lightgray; */
    padding-top: 5px;
    transition-duration: 0.5s;
    /* 열리고 닫히는 속도를 0.8초로 조정 */
    cursor: pointer;
}

/* .sub-menu: 서브 메뉴에 적용되는 클래스 선택자입니다.
height: 0: 서브 메뉴의 높이를 0으로 설정하여 처음에는 보이지 않도록 합니다.
overflow: hidden: 내용이 넘치는 경우를 대비해 내용이 넘치는 부분을 숨깁니다.
transition: height 0.5s ease: 높이가 변할 때 애니메이션 효과를 추가하여 부드러운 전환을 제공합니다.
border-top: 1px solid lightgray: 서브 메뉴의 상단에 1픽셀 두께의 연한 회색 실선 테두리를 추가합니다.
padding-top: 5px: 서브 메뉴의 상단 여백을 5픽셀로 설정합니다.
transition-duration: 0.5s: 애니메이션 전환에 소요되는 시간을 0.5초로 설정합니다. (메인 태그를 열고 닫을 때의 전환 속도 조정) */

.sub-menu.active {
    height: 180px;
}

/* .sub-menu.active: 서브 메뉴가 활성화된 상태에 적용되는 클래스 선택자입니다.
height: 100px: 서브 메뉴의 높이를 100픽셀로 설정하여 열린 상태로 표시합니다. */

.sub-items-container {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    grid-gap: 10px;
}

/* .sub-items-container: 서브 메뉴 항목들을 포함하는 컨테이너에 적용되는 클래스 선택자입니다.
display: grid: 그리드 레이아웃을 사용하여 서브 항목들을 정렬합니다.
grid-template-columns: repeat(2, 1fr): 그리드 컨테이너에서 열의 수를 2로 설정하고, 각 열의 너비를 동일하게 1fr로 설정합니다.
grid-gap: 10px: 그리드 항목 사이의 간격을 10픽셀로 설정합니다. */

.sub-item {
    margin-bottom: 5px;
    border-bottom: 1px solid lightgray;
    padding-bottom: 5px;
}

/* .sub-item: 각 서브 메뉴 항목에 적용되는 클래스 선택자입니다.
margin-bottom: 5px: 서브 항목 아래쪽에 5픽셀의 여백을 추가합니다.
border-bottom: 1px solid lightgray: 서브 항목의 하단에 1픽셀 두께의 연한 회색 실선 테두리를 추가합니다.
padding-bottom: 5px: 서브 항목의 하단 여백을 5픽셀로 설정합니다. */
</style>