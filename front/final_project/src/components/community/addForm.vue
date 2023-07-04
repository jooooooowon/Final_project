<template>
    <div>
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
                <article class="content-box1">
                    <div>
                        <input type="file" id="f1" @change="handleFile(0)">
                        <br>
                        <input type="file" id="f2" @change="handleFile(1)">
                        <br>
                        <input type="file" id="f3" @change="handleFile(2)">
                    </div>
                </article>

                <article class="content-box2">
                    <div class="tag">
                        <textarea size="30" type="text" v-model="tag" placeholder="예) #오피스룩"></textarea>
                    </div>
                </article>

                <article>
                    <div>
                        <button class="Btn" style="margin-right: 5px;" @click="add">등록</button>
                        <a href="listboard" class="Btn">이전</a>
                    </div>
                </article>
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

.content-box1 {
    margin-bottom: 5px;
}

textarea {
    width: 100%;
    height: 10em;
    border: black solid 2px;
    resize: none;
    margin-top: 10px;
}

.box {
    width: 20px;
    height: 50px;
    background-color: rgb(237, 245, 237);
    margin-right: 2px;
    display: flex;
    flex-direction: column;
    cursor: pointer;
    border: black solid 1px;
}

.active {
    background-color: rgb(3, 94, 45);
}

.addhr {
    width: 25%;
    margin-left: auto;
    margin-right: auto;
    border: solid 3px #0d5e30;
    ;
}

.Btn {
    background-color: transparent;
	border: none; 
	color:#363433;
	font-size: 1.2em;
	font-weight: bold;
    text-decoration: none;
}
</style>