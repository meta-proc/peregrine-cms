<template>
    <div>
        <div ref="trumbowyg" v-if="!schema.preview" class="wrapper wysiwygeditor">
            <trumbowyg :config="config" v-model="value"></trumbowyg>
        </div>
        <p v-else v-html="value"></p>
        <p v-if="showCharCount">Zeichen: {{ countCharacters() }}</p>
        <p v-if="showWordCount">Wörter: {{ countWords() }}</p>
    </div>
</template>

<script>
    export default {
        mixins: [ VueFormGenerator.abstractField ],
        data () {
            return {
                default: {
                    config: {
                        svgPath: '/etc/felibs/admin/images/trumbowyg-icons.svg',
                        resetCss: true,
                        btnsDef: {
                            headers: {
                                title: "Headers",
                                dropdown: [
                                    "h1",
                                    "h2",
                                    "h3",
                                    "h4"
                                ],
                                hasIcon: true,
                                ico: "h1"
                            },
                            alignments: {
                                title: "alignments",
                                dropdown: [
                                    "justifyLeft",
                                    "justifyCenter",
                                    "justifyRight"
                                ],
                                hasIcon: true,
                                ico: "justify-left"
                            }
                        },
                        btns: [
                            "removeformat",
                            "p",
                            "headers",
                            "strong",
                            "alignments",
                            "foreColor"
                        ],
                        plugins: {
                            colors: {
                                colorList: [
                                    "000000",
                                    "ffffff"
                                ]
                            },
                            cleanPaste: {
                                init: "function (trumbowyg) {trumbowyg.pasteHandlers.push(function () {setTimeout(function () {try {trumbowyg.$ed.html(cleanIt(trumbowyg.$ed.html()));} catch (c) {}}0);});"
                            }
                        }
                    }
                },
                characterCount: 0,
                wordCount: 0
            }
        },
        computed: {
            isValidBtns() {
                let btns = this.schema.config.btns
                return !btns || this.isArrayAndNotEmpty(btns)
            },
            isValidBtnsDef() {
                let btnsDef = this.schema.config.btnsDef
                return !btnsDef || this.isObjectAndNotEmpty(btnsDef)
            },
            config() {
                let cfg = this.default.config
                if (this.schema.config) {
                    let btns = this.schema.config.btns
                    if (btns && this.isValidBtns) {
                        cfg.btns = btns
                        let btnsDef = this.schema.config.btnsDef
                        if (btnsDef && this.isValidBtnsDef) {
                            cfg.btnsDef = btnsDef
                        }
                    }
                    let plugins = this.schema.config.plugins;
                    if (plugins) {
                        cfg.plugins = plugins;
                    }
                }
                return cfg;
            },
            showCharCount(){
                return this.schema.charCounter && !this.schema.readonly;
            },
            showWordCount(){
                return this.schema.wordCounter && !this.schema.readonly;
            }
        },
        mounted() {
            this.characterCount = this.$refs.trumbowyg.querySelector('.trumbowyg-editor').innerText.length;
        },
        updated() {
            this.characterCount = this.$refs.trumbowyg.querySelector('.trumbowyg-editor').innerText.length;
        },
        methods: {
            isArrayAndNotEmpty(p) {
                return Array.isArray(p) && p.length > 0
            },
            isObjectAndNotEmpty(p) {
                return typeof p === 'object' && Object.entries(p).length > 0
            },
            countCharacters() {
                let trumbowyg = this.$refs.trumbowyg;
                if(trumbowyg) {
                    this.characterCount = trumbowyg.querySelector('.trumbowyg-editor').innerText.length;
                }
                return this.characterCount;
            },
            countWords() {
                let wordsArray = "";
                let wordCount = 0;
                let trumbowyg = this.$refs.trumbowyg;
                if(trumbowyg){
                    wordsArray = trumbowyg.querySelector('.trumbowyg-editor').innerText.split(" ");
                    for(let i = 0; i < wordsArray.length; i++) {
                        let wordArray = wordsArray[i].split("\n");
                        for(let j = 0; j < wordArray.length; j++) {
                            if(wordArray[j] != "" && !this.hasWhiteSpace(wordArray[j]) ) {
                                wordCount++;
                            }
                        }
                    }
                }

                return wordCount;
            },
            hasWhiteSpace(s) {
                return /^\s+$/.test(s);
            }
        }
    }
</script>