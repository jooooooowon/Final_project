<template>
    <h3>모달 안해!!!!!!!!!!!!!!</h3>
    <hr class="addhr">
    <div class="comm-add-container">
        <div id="container">
            <section>
                <article class="photo-box">
                    <div>
                        <div v-for="(photo, index) in photos" :key="index" class="circle"
                            :class="{ active: currentPhotoIndex === index }" @click="changePhoto(index)"></div>
                    </div>
                    <div>
                        <img style="width: 250px; height: 300px;" :src="currentPhoto"><br>
                    </div>
                </article>
            </section>
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
                        <!-- <input size="30" type="text" v-model="tag" placeholder="예) #오피스룩 #남친룩"> -->
                        <textarea placeholder="내용을 입력해 주세요."></textarea>
                    </div>
                </article>
            </section>
        </div>
    </div>

    <div>
        <button style="margin-top: 5px;" @click="add">등록</button>
        <a href="listboard"><button>뒤로</button></a>

    </div>
</template>
  
<script>
export default {
    data() {
        return {
            tag: '',
            files: [],
            photos: [null, null, null],
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

/* .content-box2 {
    margin-top: 10%;
    margin-right: 50px;
  } */
.container {
    /* display: flex; */
    align-items: center;
    max-width: 30%;
}

.circle {
    width: 20px;
    height: 50px;
    /* border-radius: 50%; */
    background-color: rgb(228, 213, 213);
    margin-right: 10px;
    display: flex;
    flex-direction: column;
    cursor: pointer;
}

.active {
    background-color: rgb(24, 186, 235);
}

.addhr {
    width: 25%;
    margin-left: auto;
    margin-right: auto;
    border: solid 5px #0d5e30;
    ;
}</style>