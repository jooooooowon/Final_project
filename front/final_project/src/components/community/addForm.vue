<template>
    <div class="logo">
        <img style="width: 200px; " src="@/assets/logogo.png" alt="">
    </div>
    <hr class="addhr">
    <div class="comm-add-container">
        <div id="container">
            <!-- 박스 3개, 이미지 넣는 세션 1 -->
            <section>
                <article class="photo-box">
                    <div>
                        <div v-for="(photo, index) in photos" :key="index" class="box"
                            :class="{ active: currentPhotoIndex === index }" @click="changePhoto(index)"></div>
                    </div>
                    <div>
                        <span v-if="photos != ''">
                            <img style="width: 250px; height: 300px;" :src="currentPhoto">
                        </span>
                        <span v-if="photos == ''">
                            <img style="width: 250px; height: 300px;" src="../../assets/imageadd.png">
                        </span>
                    </div>
                </article>
            </section>
            <!-- 파일 선택, 태그, 등록,뒤로 버튼세션 2 -->
            <section class="box-container">
                    <div class="input-img">
                        <label class="input-file-button1" for="f1">첫번째 이미지</label>
                        <input type="file" id="f1" @change="handleFile(0)" style="display: none;">
                        <br>
                        <label class="input-file-button2" for="f2">두번째 이미지</label>
                        <input type="file" id="f2" @change="handleFile(1)" style="display: none;">
                        <br>
                        <label class="input-file-button3" for="f3">세번째 이미지</label>
                        <input type="file" id="f3" @change="handleFile(2)" style="display: none;">
                    </div>

                    <div class="tag">
                      <div style="font-size: 0.8em;">태그를 입력해주세요.</div>
                      <textarea type="text" v-model="tag" style="font-size: 1em; font-weight: bold;" placeholder="예) #오피스룩"></textarea>
                    </div>

                    <div class="addBtn">
                        <button class="Btn" style="margin-right: 5px;" @click="add">등록</button>
                        <a href="listboard" class="Btn">이전</a>
                    </div>
            </section>
        </div>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            tag: '',
            files: [],
            photos: [],
            currentPhotoIndex: 0,
            memnum: sessionStorage.getItem('memnum')
        }
    },
    computed: {
        currentPhoto() {
            return this.photos[this.currentPhotoIndex];
        }
    },

    methods: {
        handleFile(index) {
            const file = event.target.files[0];
            this.photos[index] = URL.createObjectURL(file);
            this.files[index] = file;
            this.currentPhotoIndex = index;
        },
        changePhoto(index) {
            this.currentPhotoIndex = index;
        },
        add() {
            const self = this;
            let formData = new FormData();
            formData.append('tag', self.tag);
            formData.append('memnum', self.memnum);
            this.files.forEach((file) => {
                formData.append('mfArr', file);

            });
            self.$axios.post('http://localhost:8081/ocommunity', formData, { headers: { "Content-Type": "multipart/form-data" } })
                .then(function () {
                    self.$router.push('/listboard');
                });
        }
    }
}
</script>

<style scoped>
body {
    margin: 0;
    padding: 0;
    max-width: 80%;
}
.logo {
    margin-top: 2%;
    margin-left: 43%;
}
#container {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

.photo-box {
    display: flex;
    flex-direction: row;
}

.box-container {
    display: flex;
    flex-direction: column;
    margin-left: 30px;
}

textarea {
    width: 95%;
    height: 8em;
    border: gray solid 1px;
    outline-color: #FE6B8B;
    resize: none;
    margin-top: 10px;
    /* margin-bottom: 10%; */
}

.box {
    width: 20px;
    height: 50px;
    /* background-color: #3363999b; */
    margin-right: 2px;
    display: flex;
    flex-direction: column;
    cursor: pointer;
    border: #224368 solid 1px;
}

.active {
    background-color: #336399;
}

.addhr {
    width: 45%;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 2%;
    border: solid 2px #336399;
    
}

.input-img {
    display: flex;
    margin-bottom: 10%;
}
.input-file-button1{
  width: 120px;
  margin-right: 10px;
  background-color:none;
  color: #336399;
  cursor: pointer;
  text-align: center;
  font-weight: bold;
}
.input-file-button2{
  width: 120px;
  margin-right: 10px;
  background-color:none;
  color: #336399;
  cursor: pointer;
  text-align: center;
  font-weight: bold;
}
.input-file-button3{
  width: 120px;
  margin-right: 10px;
  background-color:none;
  color: #336399;
  cursor: pointer;
  text-align: center;
  font-weight: bold;
}

.addBtn {
  margin-top: 50px;
  margin-left: auto;
  margin-right: auto;
}
.Btn {
  background-color: transparent;
	border: none; 
	color:#363433;
	font-size: 1em;
	font-weight: bold;
  text-decoration: none;
}
</style>