<!--
  #%L
  admin base - UI Apps
  %%
  Copyright (C) 2017 headwire inc.
  %%
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at
  
  http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  #L%
  -->
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
                            formattingWithCode: {
                                dropdown: ['p', 'quote', 'preformatted', 'h1', 'h2', 'h3', 'h4'],
                                ico: 'p', // Apply formatting icon
                                hasIcon: true
                            }
                        },
                        btns: [
                            'viewHTML',
                            'undo',
                            'redo',
                            'formattingWithCode',
                            'strong',
                            'em',
                            'superscript',
                            'subscript',
                            'link',
                            'insertImage',
                            'justifyLeft',
                            'justifyCenter',
                            'justifyRight',
                            'justifyFull',
                            'unorderedList',
                            'orderedList',
                            'removeformat'
                        ]
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
