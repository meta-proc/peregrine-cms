<template>
	<div class="wrapper">
		<input
			ref="textinput"
			class="form-control"
			v-model="value"
			:id="getFieldID(schema)"
			:disabled="disabled"
			:placeholder="schema.placeholder"
			:readonly="schema.readonly"
			type="text">
		</input>
		<p ref="cc" v-if="schema.charCount">{{ countCharacters() }}</p>
	</div>
</template>

<script>
	export default {
		mixins: [ VueFormGenerator.abstractField ],

		data() {
			return {
				characterCount: 0
			}
		},

		mounted() {
			// $(this.$refs.textarea).trigger('autoresize');
			this.characterCount = this.$refs.textinput.value.length;
		},

		methods: {
			countCharacters() {
				let textArea = this.$refs.textinput;
				if(!textArea) {
					this.characterCount = 0;
				} else {
					this.characterCount = this.$refs.textinput.value.length;
				}
				return this.characterCount;
			}
		},

		updated() {
			// if( document.activeElement != this.$refs.textarea ){
			// 	$(this.$refs.textarea).trigger('autoresize');
			// }
		}
	}
</script>