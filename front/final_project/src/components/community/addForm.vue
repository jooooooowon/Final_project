<template>
  <div id="container">
    <div v-for="(photo, index) in photos" :key="index" :class="{ active: currentPhotoIndex === index }"
      @click="changePhoto(index)"></div>

    <div v-if="step === 1" id="step-box1">
      <div class="msg">{{ msg }}</div>
      <!-- 이전버튼 -->
      <div class="box1-item1">
        <button class="box1-Btn" @click="previousImage"><span
            class="material-symbols-outlined">arrow_back_ios</span></button>
        <div class="ImgBox">
          <!-- 썸네일 이미지 -->
          <span class="Boxin" v-if="photos != ''"><img style="width: 300px; height: 325px;" :src="currentPhoto"></span>
          <!-- 등록된 이미지 -->
          <span class="Boxin" v-if="photos == ''"><img style="width: 300px; height: 325px;"
              src="../../assets/ImgAdd.png"></span>
        </div>
        <!-- 다음버튼 -->
        <button class="box1-Btn" @click="nextImage"><span
            class="material-symbols-outlined">arrow_forward_ios</span></button>
      </div>
      <!-- 이미지 등록 -->
      <div class="input-img">
        <label class="input-file-button1" for="f1"><span class="material-symbols-outlined">attach_file</span></label>

        <input type="file" id="f1" @change="handleFile(0)" style="display: none;">
        <label class="input-file-button2" for="f2"><span class="material-symbols-outlined">attach_file</span></label>

        <input type="file" id="f2" @change="handleFile(1)" style="display: none;">
        <label class="input-file-button3" for="f3"><span class="material-symbols-outlined">attach_file</span></label>

        <input type="file" id="f3" @change="handleFile(2)" style="display: none;">
      </div>
      <button class="nextBtn" @click="nextStep">Next</button>
    </div>

    <div v-else-if="step === 2" class="step-box2">
      <div class="tag">#태그를 입력해주세요.</div>
      <textarea type="text" v-model="tag" placeholder="예) #오피스룩"></textarea>
      
      <div class="addBtn">
        <button class="prevBtn" @click="prevStep">Back</button>
        <button class="Btn2" @click="add">등록</button>
      </div>
      <div class="list"><a href="listboard" class="Btn">코디 목록</a></div>
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
      memnum: sessionStorage.getItem('memnum'),
      msg: '#사진은 최대 3장까지 등록됩니다.',
      step: 1
    }
  },
  computed: {
    currentPhoto() {
      return this.photos[this.currentPhotoIndex];
    }
  },

  methods: {
    nextStep() {
      this.step++;
    },
    prevStep() {
      this.step--;
    },
    handleFile(index) {
      const file = event.target.files[0];
      this.photos[index] = URL.createObjectURL(file);
      this.files[index] = file;
      this.currentPhotoIndex = index;
    },
    changePhoto(index) {
      this.currentPhotoIndex = index;
    },
    previousImage() {
      if (this.currentPhotoIndex > 0) {
        this.currentPhotoIndex--;
      }
    },
    nextImage() {
      if (this.currentPhotoIndex < this.photos.length - 1) {
        this.currentPhotoIndex++;
      }
    },
    add() {
      const self = this;
      let formData = new FormData();
      formData.append('tag', self.tag);
      formData.append('memnum', self.memnum);
      this.files.forEach((file) => {
        formData.append('mfArr', file);

      });
      self.$axios.post('http://localhost:7878/ocommunity', formData, { headers: { "Content-Type": "multipart/form-data" } })
        .then(function () {
          self.$router.push('/listboard');
        });
    }
  }
}

</script>

<style scoped>
@font-face {
  font-family: 'PyeongChang-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
  font-weight: normal;
  font-style: normal;
}

/* 전체 박스 */
#container {
  font-family: 'PyeongChang-Regular';
  font-weight: 400;
  max-width: 40%;
  margin: 0 auto;
}

#step-box1 {
  display: flex;
  flex-direction: column;
}

.msg {
  margin-top: 5%;
  margin-left: 33%;
  margin-bottom: -5%;
}

.box1-item1 {
  display: flex;
  justify-content: center;
}

.box1-Btn {
  background-color: transparent;
  border: none;
  margin-left: 15px;
  margin-right: 15px;
}

.ImgBox {
  width: 400px;
  height: 425px;
  position: relative;
}

.Boxin {
  position: absolute;
  inset: 12%;
}

.Boxin img {
  object-fit: cover;
}

.input-img {
  display: flex;
  justify-content: center;
  margin-top: -3%;
}

.material-symbols-outlined {
  cursor: pointer;
  font-size: 2em;
  color: rgb(165, 164, 164);
}

.nextBtn {
  width: 10%;
  display: flex;
  justify-content: center;
  margin-top: 30px;
  margin-left: 517px;
  background-color: transparent;
  border: none;
  color: gray;
  font-size: 1.7em;
  font-weight: bold;
  cursor: pointer;
}

.nextBtn:hover {
  color: rgb(41, 108, 28);
}

.step-box2 {
  display: flex;
  flex-direction: column;
  margin: 0 auto;
  margin-top: 15%
}
textarea {
  width: 80%;
  height: 6.25em;
  border: solid rgba(128, 128, 128, 0.327) 0.5px;
  resize: none;
  font-size: 1.3em;
  font-weight: 800;
  margin: 0 auto;
  margin-top: 3%;
}
textarea:focus {
  outline: 2px solid rgb(16, 145, 16);
}
.tag {
  font-size: 1.3em;
  margin-bottom: 5px;
  font-weight: bold;
  margin: 0 auto;
}

.addBtn {
  display: flex;
  margin-bottom: 5%;
}
.prevBtn {
  background-color: transparent;
  border: none;
  margin-left: 10%;
  font-size: 1.7em;
  font-weight: bold;
  color: gray;
  cursor: pointer;
}
.prevBtn:hover {
  color: rgb(41, 108, 28);
}
.Btn2 {
  background-color: transparent;
  border: none;
  margin-left: 60%;
  cursor: pointer;
  font-size: 1.5em;
  font-weight: bolder;
  color: gray;
}
.Btn2:hover {
  color: rgb(41, 108, 28);
}
.list {
  margin-top: 5%;
  margin: 0 auto;
}
.Btn {
  background-color: transparent;
  border: none;
  /* text-decoration: none; */
  cursor: pointer;
  font-size: 1.2em;
  font-weight: bolder;
  color: black;
}

.Btn:hover {
  color: rgb(16, 145, 16);
}</style>