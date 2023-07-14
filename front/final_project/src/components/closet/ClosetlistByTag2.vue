<template>
    <div class="body-css">
        <div class="search">
            <input type="text" v-model="cloth" size="30" style="font-family: 'PyeongChang-Regular'; height: 30px; text-align: center; border-color: lightgray;"
                placeholder="옷 검색하기" onfocus="this.placeholder=''" onblur="this.placeholder='옷 검색하기'">
            <span class="searchBtn"><button v-on:click="clothserach" style="font-family: 'PyeongChang-Regular';">검색</button></span>
            <div class="addCloth">
                <button v-on:click="modalOpenAdd" style="font-family: 'PyeongChang-Regular'; width:130px; height:38px;" class="addBtn">내옷 등록하기</button>
            </div>
        </div>

       <!-- 옷장에 옷 등록하기 모달창 -->
       <div class="modal-wrap-add" v-show="modalCheckAdd" @click="modalCloseAdd" id="modalWrapAdd">
            <!-- :class 동적 클래스 바인딩.. 조건에 따라 클래스를 동적으로 조작 -->
            <div class="modal-container-add" @click.stop="" id="containerAdd" :class="{ expanded: isExpanded }">
                <!-- file drag & drop -->
                <div class="cloth-add-file-container">
                    <div class="file-upload-container" @dragenter="onDragenter" @dragover="onDragover"
                        @dragleave="onDragleave" @drop="onDrop" @click="onClick">
                        <label for="imgtag" id="imglabel">
                            <span v-if="addThumbnailfile != ''">
                                <img class="modal-img-add" id="addthumbimg" :src="addThumbnailfile"
                                    style="width:382px; height:382px; cursor: pointer;">
                            </span>
                            <span v-if="addThumbnailfile == ''">
                                <img class="modal-img-add" id="addthumbimg" src="../../assets/dnd.png"
                                    style="width:382px; height:382px; cursor: pointer;">
                            </span>
                        </label>
                        <input type="file" id="imgtag" style="display:none" ref="fileInput" class="file-upload-input"
                            accept="image/*" @change="onFileChange">
                    </div>
                    <!-- <span> {{ file.src }}</span> -->
                </div>
                <div class="cloth-add-info-container" v-if="isExpanded">
                    내옷 상위분류
                    <select v-model="selectedmain" @change="updatesub" style="font-family: 'PyeongChang-Regular'; width:150px">
                        <option v-for="maintag in addmaintags" v-bind:value="maintag" v-bind:key="maintag">{{ maintag }}
                        </option>
                    </select><br />
                    내옷 하위분류
                    <select v-model="selectedsub" style="font-family: 'PyeongChang-Regular'; width:150px">
                        <option v-for="subtag in addsubtags" v-bind:value="subtag" v-bind:key="subtag">{{ subtag }}</option>
                    </select><br />
                    내옷 별명
                    <input type="text" v-model="clothname" size="16" style="font-family: 'PyeongChang-Regular'; height:25px"><br />
                    <div class="cloth-add-button-container">
                        <!-- 등록하기 버튼 -->
                        <button v-on:click="addcloset">등록</button> |
                        <button v-on:click="modalCloseAdd">취소</button>
                    </div>
                </div>
                <!-- 기본 이미지 등록 버튼 -->
                <div class="basic-button-container" v-if="!addThumbnailfile && !isExpanded">
                    <button class="next-button" @click="addBasicImage">기본 이미지 등록</button>
                </div>
                <!-- 다음 버튼 -->
                <div class="next-button-container" v-if="addThumbnailfile && !isExpanded">
                    <button class="next-button" @click="expandModal">다음</button>
                </div>
                <!-- 이전 버튼 -->
                <div class="before-button-container" v-if="addThumbnailfile && isExpanded">
                    <button class="reduce-button" @click="reduceModal">이전</button>
                </div>
            </div>
        </div>

        <!-- 옷장 좌측 메뉴바 -->
        <div ref="stickyPoint">
            <div class="menu-wrapper" id="menu-wrapper" :class="{ sticky: isSticky }">
                <div class="menu-bar">
                    <div v-for="(item, index) in menuItems" :key="index" class="menu-item">
                        <div class="main-tag" @mousedown="toggleSubmenu(index)" v-on:click="getall(index)">&nbsp;&nbsp;{{
                            item.title }}
                            <span class="main-tag-eng" v-if="index == 1">&nbsp;Outer</span>
                            <span class="main-tag-eng" v-if="index == 2">&nbsp;Top</span>
                            <span class="main-tag-eng" v-if="index == 3">&nbsp;Pants</span>
                            <span class="main-tag-eng" v-if="index == 4">&nbsp;etc</span>
                            <span class="main-tag-eng" v-if="index == 5">&nbsp;Shoes</span>

                            <div class="main-tag-icon" v-if="index != 0 && item.isOpen == false">
                                <img src="../../assets/plus.png">
                            </div>
                            <div class="main-tag-icon" v-if="index != 0 && item.isOpen == true">
                                <img src="../../assets/minus.png">
                            </div>
                        </div>
                        <div class="sub-menu" :class="{ active: item.isOpen }">
                            <div class="sub-items-container">
                                <div v-for="(subtag, subIndex) in item.subItems" :key="subIndex" class="sub-item"
                                    v-on:click="listbytag(subtag, subIndex)"
                                    style="font-size: 12px; font-weight: 600; color:rgb(142, 140, 140)">
                                    {{ subtag }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 옷장에 등록된 옷 리스트 -->
        <div v-if="isList == true" class="main-list">
            <div class="container" v-for="(row, index) in additionalCloset" :key="index"
                style="display: flex; align-items: center;">
                <div class="card" v-for="closet in row" :key="closet.closetnum">
                    <span v-if="closet.img != 'basicImage'">
                    <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + closet.closetnum"
                        v-on:click="modalOpenDetail(closet.closetnum)" style="cursor: pointer;" title="수정하려면 클릭하세요.">
                    </span>
                    <span v-if="closet.img == 'basicImage'">
                        <img src="../../assets/basic.png"
                        v-on:click="modalOpenDetail(closet.closetnum)" style="cursor: pointer;" title="수정하려면 클릭하세요.">
                    </span>
                    <div class="favImg">
                        <a v-on:click="favorite(closet.closetnum)">
                            <span v-if="closet.favorite == 1">
                                <img src="../../assets/FullStar.png" alt="">
                            </span>
                            <span v-else>
                                <img src="../../assets/emptyStar.png" alt="">
                            </span>
                        </a>
                    </div>
                    <div class="inform">
                        {{ closet.maintag }}&nbsp;|&nbsp;{{ closet.subtag }}
                        <br /><br />
                        <a v-on:click="modalOpenDetail(closet.closetnum)">{{ closet.cloth }}</a><br />
                        <!-- v-on:click="detail(closet.closetnum)" -->
                    </div><br />
                    <div class="btn-container">
                        <button v-on:click="deletecloth(closet.closetnum, closet.favorite)">삭제</button>
                    </div>
                </div>
            </div>
            <span class="more-btn"><button v-on:click="moreBtn">더보기</button></span><br />
        </div><br />
        <div v-if="isList == false">등록된 옷이 없습니다.</div>

        <!-- 옷 디테일 모달창 -->
        <div class="modal-wrap-detail" v-show="modalCheckDetail" @click="modalCloseDetail" id="modalWrapDetail">
            <div class="modal-container-detail" @click.stop="" id="containerDetail">
                <label for="detailEditFile">
                    <span v-if="img != 'basicImage' && detailEditImg == ''">
                        <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + setClosetnum"
                            class="modal-img-detail">
                    </span>
                    <span v-if="img != 'basicImage' && detailEditImg != ''">
                        <img :src="detailEditImg" class="modal-img-detail">
                    </span>
                    <span v-if="img == 'basicImage' && detailEditImg == ''">
                        <img src="../../assets/basic.png"
                        class="modal-img-detail">
                    </span>
                    <span v-if="img == 'basicImage' && detailEditImg != ''">
                        <img :src="detailEditImg" class="modal-img-detail">
                    </span>
                </label>
                <input type="file" id="detailEditFile" style="font-family: 'PyeongChang-Regular'; display: none" accept="image/*" @change="thumbnailChange">
                <div class="modal-tags-detail">{{ maintag }} | {{ sub }}</div>
                <div class="modal-search-detail"><input type="text" v-model="modalCloth" size="15"
                        style="height: 38px; text-align: center;"></div>
                        <div class="modal-btn-detail"><button v-on:click="change(setClosetnum)" style="font-family: 'PyeongChang-Regular'; width:65px; height:38px;">수정</button></div>
            </div>
        </div>
        <div class="naver-shopping">
            <router-link to="naverShoppingList"><img src="../../assets/navershopping2.png"
                    alt="naver-shopping-icon"></router-link>
        </div>
    </div>

    <br />
</template>

<script>
export default {
    name: 'ClosetlistByTag',
    data() {
        return {
            tag: this.$route.query.tag,
            index: this.$route.query.index,
            closetlist: [],
            additionalCloset: [],
            closetPerPage: 5,
            currentPage: 3,
            subtags: [],
            cloth: '',
            memnum: sessionStorage.getItem('memnum'),
            isList: '',
            // 메뉴바
            isSticky: false,
            // add 모달
            addThumbnailfile: '', // 업로드된 이미지 썸네일
            addfile: '', // 업로드된 이미지 파일(서버로 보낼)
            modalCheckAdd: false,
            selectedmain: '',
            selectedsub: '',
            addmaintags: ['아우터', '상의', '하의', '기타', '신발'],
            addsubtags: '',
            clothname: '',
            isExpanded: false, // 모달창 확장 여부를 나타내는 변수 추가
            // detail 모달
            setClosetnum: null,
            modalCloth: '',
            maintag: '',
            sub: '',
            img: null, // 기본이미지 유무 체크용
            modalCheckDetail: false,
            detailEditImg: '', // 디테일 이미지 수정 썸네일 이미지
            uploadimg: '', // 디테일 -> 이미지수정 -> 바뀐 이미지 주소 담는 변수
            thumbimg: '',
            menuItems: [
                { title: "전체", isOpen: false, subItems: [] },
                { title: "아우터", isOpen: false, subItems: ['아우터(전체)', '가디건', '자켓', '야상', '트렌치코트', '코트', '패딩', '아우터(기타)'] },
                { title: "상의", isOpen: false, subItems: ['상의(전체)', '민소매', '반팔', '긴팔티', '셔츠', '니트', '맨투맨', '상의(기타)'] },
                { title: "하의", isOpen: false, subItems: ['하의(전체)', '반바지', '치마', '면바지', '청바지', '레깅스', '하의(기타)'] },
                { title: "기타", isOpen: false, subItems: ['기타(전체)', '스타킹', '히트텍', '기모제품', '방한용품', 'etc'] },
                { title: "신발", isOpen: false, subItems: ['신발(전체)', '샌들', '슬리퍼', '운동화', '등산화', '구두', '신발(기타)'] }
            ]
        }
    },
    mounted() {
        window.addEventListener('scroll', this.stickyScroll);
        this.stickyScroll();
    },
    created: function () { // 해당 컴포넌트가 처음 실행될 때만 적용... 그 다음부터는 변경된 컴포넌트(같은 컴포넌트로 이동할 때 적용이 안됨)
        const self = this;
        self.memnum = sessionStorage.getItem('memnum')
        if(self.memnum == null) {
            alert('로그인 화면으로 이동합니다.')
            location.href = '/login'
        } else {
            if (self.index == 0) {
                let maintag = self.$route.query.tag.split('(', 1);
                self.$axios.get('http://localhost:7878/closets/maintags/' + self.memnum + "/" + maintag)
                    .then(function (res) {
                        if (res.status == 200) {
                            self.closetlist = res.data.list;
                            if (self.closetlist != '') {
                                // self.checkMemnum = self.closetlist[0].memnum.memnum;
                                self.isList = true;
                                const addtionalRow1 = self.closetlist.slice(0, self.closetPerPage);
                                const addtionalRow2 = self.closetlist.slice(self.closetPerPage, self.closetPerPage * 2);
                                const addtionalRow3 = self.closetlist.slice(self.closetPerPage * 2, self.closetPerPage * 3);
                                self.additionalCloset.push(addtionalRow1, addtionalRow2, addtionalRow3);
                                // self.additionalCloset.push(addtionalRow1);
                            } else {
                                self.isList = false;
                            }
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            } else {
                let subtag = this.$route.query.tag;
                self.$axios.get('http://localhost:7878/closets/subtags/' + self.memnum + "/" + subtag)
                    .then(function (res) {
                        if (res.status == 200) {
                            // 컴포넌트 처음 로딩될 때 옷장에서 999999999번 default 걸러서 리스트에 넣기
                            self.closetlist = res.data.list;
                            if (self.closetlist != '') {
                                // self.checkMemnum = self.closetlist[0].memnum.memnum;
                                self.isList = true;
                                const addtionalRow1 = self.closetlist.slice(0, self.closetPerPage);
                                const addtionalRow2 = self.closetlist.slice(self.closetPerPage, self.closetPerPage * 2);
                                const addtionalRow3 = self.closetlist.slice(self.closetPerPage * 2, self.closetPerPage * 3);
                                self.additionalCloset.push(addtionalRow1, addtionalRow2, addtionalRow3);
                                // self.additionalCloset.push(addtionalRow1);
                            } else {
                                self.isList = false;
                            }
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        }
    },
    methods: {
        stickyScroll() {
            const stickyPoint = this.$refs.stickyPoint;
            if (!stickyPoint) {
                return; // stickyPoint가 정의되지 않은 경우 종료
            } else {
                const menuOffsetTop = stickyPoint.offsetTop;
                if (window.pageYOffset > menuOffsetTop) {
                    this.isSticky = true;
                    // this.$nextTick(() => {
                    stickyPoint.style.top = '10px'; // 원하는 간격으로 조정
                    // });
                } else {
                    this.isSticky = false;
                    // this.$nextTick(() => {
                    // stickyPoint.style.top = '-30px'; // 초기 위치로 설정
                    // });
                }
            }
        },
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
            const startIndex = self.currentPage * self.closetPerPage;
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
        deletecloth(closetnum, favorite) {
            const self = this;
            if (favorite == 1) {
                let answer = confirm('즐겨찾기된 옷입니다. 정말 삭제하시겠습니까?')
                if (answer) {
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
                } else {
                    alert('삭제가 취소되었습니다.')
                }
            } else {
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
            }
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
        listbytag(subtag, index) {
            const self = this;
            self.$router.push({ name: 'ClosetListByTag', query: { tag: subtag, index: index } });
        },
        clothserach() {
            const self = this;
            if (self.cloth == '') {
                alert('검색할 옷을 입력해주세요.');
            } else {
                self.$router.push({ name: 'ClosetListByCloth', query: { cloth: self.cloth } });
            }
        },
        // 이미지를 업로드한 후에 다음 버튼을 보이도록 확장하는 메서드
        expandModal() {
            this.isExpanded = true;
            let upload = document.getElementsByClassName('file-upload-container')[0].style;
            upload.pointerEvents = 'none';
            document.getElementById('addthumbimg').disabled = true;
        },
        // 이전 버튼 누르면 모달창 다시 축소
        reduceModal() {
            this.isExpanded = false;
            let upload = document.getElementsByClassName('file-upload-container')[0].style;
            upload.pointerEvents = 'auto';
            document.getElementById('addthumbimg').disabled = false;

        },
        // add 모달창 열기
        modalOpenAdd() {
            const self = this;
            self.modalCheckAdd = !self.modalCheckAdd;
        },
        // add 모달창 닫기
        modalCloseAdd() {
            const self = this;
            let check = '';
            if (self.addThumbnailfile != '' || self.selectedmain != '' || self.selectedsub != '' || self.clothname != '') {
                check = confirm("게시글 작성을 취소하시겠습니까?");
                if (check) {
                    self.addThumbnailfile = '';
                    self.selectedmain = '';
                    self.selectedsub = '';
                    self.clothname = '';
                    this.isExpanded = false;
                    let upload = document.getElementsByClassName('file-upload-container')[0].style;
                    upload.pointerEvents = 'auto';
                    this.modalCheckAdd = !this.modalCheckAdd;
                }
            } else {
                self.addThumbnailfile = '';
                self.selectedmain = '';
                self.selectedsub = '';
                self.clothname = '';
                this.isExpanded = false;
                let upload = document.getElementsByClassName('file-upload-container')[0].style;
                upload.pointerEvents = 'auto';
                this.modalCheckAdd = !this.modalCheckAdd;
            }
        },
        // add 모달창 안에서 file drag & drop 
        // onClick() {
        //     this.$refs.fileInput.click()
        // },
        onDragenter(event) {
            // class 넣기
            event.isDragged = true
        },
        onDragleave(event) {
            // class 삭제
            event.isDragged = false
        },
        onDragover(event) {
            // 드롭을 허용하도록 prevetDefault() 호출
            event.preventDefault()
        },
        onDrop(event) {
            // 기본 액션을 막음 (링크 열기같은 것들)
            event.preventDefault()
            this.isDragged = false
            const files = event.dataTransfer.files
            this.addFiles(files)
        },
        onFileChange(event) {
            const files = event.target.files
            this.addFiles(files)
        },
        addFiles(files) {
            if (files[0] != null) {
                const reader = new FileReader();
                const self = this;
                reader.onload = function () {
                    self.addThumbnailfile = reader.result;
                    self.addfile = files[0];
                }
                reader.readAsDataURL(files[0]);


                // const src = await this.readFiles(files[0])
                // files[0].src = src
                // this.file = files[0];
                // console.log(this.file)
            }
        },
        // // FileReader를 통해 파일을 읽어 thumbnail 영역의 src 값으로 셋팅
        // async readFiles(files) {
        //     return new Promise((resolve) => {
        //         const reader = new FileReader()
        //         reader.onload = async (e) => {
        //             resolve(e.target.result)
        //         }
        //         reader.readAsDataURL(files)
        //     })
        // },
        updatesub() {
            this.selectedsub = '';
            if (this.selectedmain == '아우터') {
                this.addsubtags = ['가디건', '자켓', '야상', '트렌치코트', '코트', '패딩', '아우터(기타)']
            } else if (this.selectedmain == '상의') {
                this.addsubtags = ['민소매', '반팔', '긴팔티', '셔츠', '니트', '맨투맨', '상의(기타)']
            } else if (this.selectedmain == '하의') {
                this.addsubtags = ['반바지', '치마', '면바지', '청바지', '레깅스', '하의(기타)']
            } else if (this.selectedmain == '기타') {
                this.addsubtags = ['스타킹', '히트텍', '기모제품', '목도리', 'etc']
            } else if (this.selectedmain == '신발') {
                this.addsubtags = ['샌들', '슬리퍼', '운동화', '등산화', '구두', '신발(기타)']
            }
        },
        addBasicImage() {
            const self = this;
            self.addThumbnailfile = require('../../assets/basic.png');
        },
        addcloset() {
            const self = this;
            let formdata = new FormData();
            if (self.addThumbnailfile == '') {
                alert("옷 이미지를 등록해주세요.")
            } else if (self.selectedmain == '' || self.selectedsub == '' || self.clothname == '') {
                alert("등록하시는 옷의 태그 또는 이름을 정해주세요.")
            } else {
                if(self.addfile == ''){
                    formdata.append('memnum', self.memnum)
                    formdata.append('cloth', self.clothname)
                    formdata.append('maintag', self.selectedmain)
                    formdata.append('subtag', self.selectedsub)
                    self.$axios.post('http://localhost:7878/closets/nofile', formdata)
                        .then(function () {
                            location.reload();
                        })                    
                } else {
                    formdata.append('f', self.addfile)
                    formdata.append('memnum', self.memnum)
                    formdata.append('cloth', self.clothname)
                    formdata.append('maintag', self.selectedmain)
                    formdata.append('subtag', self.selectedsub)
                    self.$axios.post('http://localhost:7878/closets', formdata)
                        .then(function () {
                            location.reload();
                        })
                }
            }
        },
        // detail 모달창 열기
        modalOpenDetail(closetnum) {
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

        // 옷 디테일 모달에서 옷 수정하기
        change(closetnum) {
            const self = this;
            let formdata = new FormData();
            if (self.uploadimg == null) {
                self.$axios.patch('http://localhost:7878/closets/editcloth/' + closetnum + "/" + self.modalCloth)
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
                self.$axios.patch('http://localhost:7878/closets/editcloth/' + closetnum + "/" + self.modalCloth, formdata)
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
        thumbnailChange(event) {
            const editImg = event.target.files;
            this.editDetailImg(editImg);
        },
        editDetailImg(editImg) {
            const self = this;
            if (editImg[0] != null) {
                const reader = new FileReader();
                reader.onload = function () {
                    self.detailEditImg = reader.result;
                    self.uploadimg = editImg[0];
                };
                reader.readAsDataURL(editImg[0]);
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
    text-align: center;
}

.search {
    margin-top: 40px;
    margin-bottom: 10px;
    margin-left: 60px;
}

.search input[type="text"] {
    border-radius: 5px;
    font-size: 15px;
}


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

/* 옷장 리스트 */
.main-list {
    width: 950px;
    position: absolute;
    margin: auto;
    left: 0;
    right: 0;
}

.container {
    /* display: flex; */
    /* flex-wrap: wrap;
    justify-content: center; */
    margin-top: 0px;
}

.card {
    width: 150px;
    height: 252px;
    left: 165px;
    background-color: white;
    /* border-color: rgb(222, 222, 222); */
    border: 1px solid rgb(222, 222, 222);
    padding: 20px;
    margin: 10px;
    font-size: 15px;
    text-align: center;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    transition: transform 0.3s ease;
    align-items: center;
}

.card:hover {
    background-color: rgb(242, 242, 242);
    transform: scale(1.05);
}

.card img {
    position: relative;
    width: 150px;
    /* 가로 사이즈 200px로 고정 */
    height: 150px;
    /* 세로 사이즈 200px로 고정 */
    object-fit: cover;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    border-radius: 3px;
}

.favImg {
    margin-left: 150px;
    /* width: 50px; */
    /* height: 50px; */
    cursor: pointer;
}

.favImg img {
    width: 50px;
    height: 50px;
}

a {
    cursor: pointer;
}

.inform {
    /* margin-left: 150px; */
    margin-top: -35px;
}

button {
    border: 1px solid rgb(177, 177, 177);
    background-color: white;
    /* background-color: rgb(196, 215, 178, 0.6); */
    /* border: none; */
    border-radius: 5px;
    transition: .5s;
    font-size: 15px;
    font-weight: normal;
    font-family: 'PyeongChang-Regular';
}

button:hover {
    background-color: #85b380;
    color: #ffffff;
    /* color: #85b380; */
    cursor: pointer;
    /* font-weight: bold; */
}

.more-btn {
    position: relative;
    top: 0px;
    right: -54px;
    padding-bottom: 50px;
}

.more-btn button {
    width: 80px;
    height: 35px;
}


.addCloth {
    margin-left: 955px;
    margin-top: -40px;
}

.searchBtn {
    margin-left: 5px;
}

.searchBtn button {
    width: 65px;
    height: 35px;
}

.btn-container {
    display: flex;
    flex-wrap: wrap;
    /* 필요한 경우 버튼이 줄바꿈되도록 함 */
    justify-content: space-between;
    border-top: 1px solid lightgray;
    width: 80px;

}

.btn-container button {
    border: none;
    padding: 5px 10px;
    cursor: pointer;
    flex: 1 0 45%;
    /* 버튼의 크기 조정 */
    margin: 5px;
    /* 버튼 사이의 간격 설정 */
    height: 33px;
    background-color: transparent;
}

.btn-container button:hover {
    color: #85b380;
}

/* ----- 옷 등록 모달창 ----- */
.modal-wrap-add {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    z-index: 2;
}

.modal-container-add {
    /* overflow: auto; */
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 400px;
    height: 53%;
    background: #fff;
    box-shadow: 0 20px 10px rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    padding: 20px;
    box-sizing: border-box;
    transition: width 0.3s ease;
    /* display: flex; */
    /* 가운데정렬 */
    /* flex-direction: column; */
    /* 내용 수직으로 배치 */
    /* justify-content: center; */
    /* 수직방향 가운데 정렬 */
    /* align-items: center; */
    /* 수평방향 가운데 정렬 */
}

.modal-container-add.expanded {
    width: 750px;
    /* 우측에 폼이 펼쳐지면서 모달창이 가로 800px로 확장됨 */
}

.cloth-add-button-container button {
    width: 50px;
    height: 35px;
}
.basic-button-container {
    position: absolute;
    top: 275px;
    right: 140px;
}
.next-button-container {
    position: absolute;
    top: 11px;
    right: 12px;
    /* 우측 상단에 위치 */
    /* 기타 스타일 생략 */
}

.next-button-container button {
    width: 50px;
    height: 35px;
}

.before-button-container {
    position: absolute;
    top: 11px;
    right: 12px;
    /* 우측 상단에 위치 */
    /* 기타 스타일 생략 */
}

.before-button-container button {
    width: 50px;
    height: 35px;
}

.cloth-add-file-container {
    position: relative;
    display: flex;
    width: 382px;
    height: 382px;
    right: 10px;
    bottom: 14px;
}

.cloth-add-info-container {
    position: relative;
    width: 330px;
    height: 265px;
    bottom: 93%;
    left: 54.5%;
    border: 1px solid lightgray;
    display: flex;
    flex-direction: column;
    /* 내용 수직으로 배치 */
    justify-content: center;
    /* 수직방향 가운데 정렬 */
    align-items: center;
    /* 수평방향 가운데 정렬 */
}

/* .cloth-add-button-container {
    position: relative;
    width: 250px;
    bottom: 72%;
    left: 60%;
    top: 30%;
} */

.modal-btn-add {
    margin-bottom: -110px;
}

.modal-search-add {
    margin-bottom: 10px;
}

.modal-img-add {
    /* position: relative; */
    width: 370px;
    /* 가로 사이즈 200px로 고정 */
    height: 370px;
    /* 세로 사이즈 200px로 고정 */
    object-fit: cover;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    border-radius: 5px;
    /* margin-right: 350px; */
    cursor: pointer;
}

.modal-tags-add {
    margin-top: -5px;
    margin-bottom: 5px;
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
    cursor: pointer;
}

.modal-tags-detail {
    margin-top: -5px;
    margin-bottom: 5px;
}


/* ----- 메뉴바 ----- */

.menu-wrapper {
    position: absolute;
    top: 270px;
    left: 10px;
    width: 14%;
    z-index: 1;
    /* box-shadow: 0 10px 10px rgba(0, 0, 0, 0.1); */
}

/* .menu-wrapper: 메뉴 바를 감싸는 컨테이너에 적용되는 클래스 선택자입니다.
position: fixed: 요소를 페이지에서 고정 위치로 설정합니다.
top: 0: 컨테이너를 페이지의 상단에 위치시킵니다.
left: 0: 컨테이너를 페이지의 왼쪽에 위치시킵니다.
width: 20%: 컨테이너의 너비를 부모 요소의 너비의 20%로 설정합니다.
z-index: 100: 컨테이너의 층위를 설정하여 다른 요소 위에 나타나도록 합니다. */

.menu-wrapper.sticky {
    position: fixed;
    top: 10px;
    transition: top 1.5s ease;
}

.menu-bar {
    position: relative;
    background-color: white;
    /* border: 1px solid rgb(222, 222, 222); */
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
    width: 100%;
    padding: 12px;
}

/* .menu-bar: 메뉴 바 전체에 적용되는 클래스 선택자입니다.
width: 20%: 메뉴 바의 너비를 부모 요소의 너비의 20%로 설정합니다.
background-color: lightgray: 메뉴 바의 배경색을 연한 회색으로 설정합니다.
border: 1px solid lightgray: 메뉴 바의 테두리를 1픽셀 두께의 연한 회색 실선으로 설정합니다.
padding: 5px: 메뉴 바의 안쪽 여백을 5픽셀로 설정합니다. */

.menu-item {
    margin-bottom: 15px;
}

/* .menu-item: 각 메뉴 항목에 적용되는 클래스 선택자입니다.
margin-bottom: 10px: 메뉴 항목 아래쪽에 10픽셀의 여백을 추가합니다. */

.main-tag {
    cursor: pointer;
    text-align: left;
    font-weight: bold;
    color: rgb(90, 89, 89);
    font-size: 14px;
}


/* .main-tag: 메뉴 항목의 메인 태그에 적용되는 클래스 선택자입니다.
cursor: pointer: 마우스 커서를 가리킬 때 포인터 모양으로 변경합니다. */
.main-tag-eng {
    font-size: 13px;
    font-weight: normal;
    color: rgb(169, 169, 169)
}

.main-tag-icon {
    position: relative;
    width: 20px;
    bottom: 15px;
    left: 205px;
}

.main-tag-icon img {
    width: 9px;
    height: 9px;
}

.sub-menu {
    height: 0;
    overflow: hidden;
    transition: height 0.5s ease;
    /* border-bottom: 1px solid lightgray; */
    padding-top: 10px;
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
    height: 140px;
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
    margin-bottom: 0px;
    border-bottom: 1px solid lightgray;
    padding-bottom: 10px;
}

/* .sub-item: 각 서브 메뉴 항목에 적용되는 클래스 선택자입니다.
margin-bottom: 5px: 서브 항목 아래쪽에 5픽셀의 여백을 추가합니다.
border-bottom: 1px solid lightgray: 서브 항목의 하단에 1픽셀 두께의 연한 회색 실선 테두리를 추가합니다.
padding-bottom: 5px: 서브 항목의 하단 여백을 5픽셀로 설정합니다. */

/* 네이버 쇼핑 */
.naver-shopping img {
    position: fixed;
    right: -26px;
    bottom: -30px;
    margin: 70px;
    z-index: 1;
    overflow: hidden;
    box-shadow: 0 10px 30px rgba(0, 0, 0, .45);
    transition: .5s;
    border-radius: 50%;
    width: 80px;
    height: 80px;
}

.naver-shopping:hover img {
    bottom: 15px;
}
</style>