<template>


    <div id="container">
        <div v-for="(photo, index) in photos" :key="index" :class="{ active: currentPhotoIndex === index }"
          @click="changePhoto(index)"></div>
        <div id="box1">
          <!-- 이전버튼 -->
          <button class="box1-Btn" @click="previousImage"><span class="material-symbols-outlined">arrow_back_ios</span></button>
          <!-- 썸네일 이미지 -->
          <span v-if="photos != ''">
            <img style="width: 250px; height: 300px;" :src="currentPhoto">
          </span>
          <!-- 등록된 이미지 -->
          <span v-if="photos == ''">
            <img style="width: 250px; height: 300px;" src="../../assets/ImgAdd.png">
          </span>
          <!-- 다음버튼 -->
          <button class="box1-Btn" @click="nextImage"><span class="material-symbols-outlined">arrow_forward_ios</span></button>
        </div>
        <!-- 이미지 등록 -->
        <div class="input-img">
          <label class="input-file-button1" for="f1">
            <span class="material-symbols-outlined">attach_file</span>
          </label>
          <input type="file" id="f1" @change="handleFile(0)" style="display: none;">
          <label class="input-file-button2" for="f2">
            <span class="material-symbols-outlined">attach_file</span>
          </label>
          <input type="file" id="f2" @change="handleFile(1)" style="display: none;">
          <label class="input-file-button3" for="f3">
            <span class="material-symbols-outlined">attach_file</span>
          </label>
          <input type="file" id="f3" @change="handleFile(2)" style="display: none;">
        </div>

      <div id="box2">
        <div>
          <div class="tag">#태그를 입력해주세요.</div>
          <textarea type="text" v-model="tag" placeholder="예) #오피스룩"></textarea>
        </div>

        <div class="addBtn">
          <span><button class="Btn" style="margin-right: 5px;" @click="add">등록</button></span>
          <span><a href="listboard" class="Btn">이전</a></span>
        </div>
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
      self.$axios.post('http://localhost:8081/ocommunity', formData, { headers: { "Content-Type": "multipart/form-data" } })
        .then(function () {
          self.$router.push('/listboard');
        });
    }
  }
}

</script>

<style scoped>
#container {
  display: flex;
  flex-direction: column;
  align-content: center;
}
#box1 {
  display: flex;
  justify-content: center;
}

.box1-Btn {
  background-color:transparent;
	border: none;
}

.input-img {
  display: flex;
  justify-content: center;
  
}
.material-symbols-outlined {
  cursor: pointer;
  font-size: 2em;
  color: rgb(165, 164, 164);
}
#box2 {
  display: flex;
  flex-direction: column;
  margin-left: 630px;
  margin-top: 10px;
  /* margin-right: auto; */
}

textarea {
  width: 30%;
  height: 6.25em;
  border: solid rgba(128, 128, 128, 0.327) 0.5px;
  resize: none;
  font-size: 1.1em;
}
textarea:focus {
	outline: 2px solid rgb(16, 145, 16);
}
.tag {
  font-size: 1em; 
  margin-bottom: 5px;
  font-weight: bold;
}
.addBtn {
  margin-left: 100px;
}
.Btn {
  background-color:transparent;
	border: none;
  text-decoration: none;
  cursor: pointer;
  font-size: 1em;
  font-weight: bolder;
  color: black;
}
.Btn:hover {
  color: rgb(16, 145, 16);
}
</style>