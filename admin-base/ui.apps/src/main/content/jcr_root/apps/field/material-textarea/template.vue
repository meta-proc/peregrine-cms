<template>
	<div class="wrapper">
		<div v-if="!schema.preview" class="input-field">
			<textarea
					ref="textarea"
					class="form-control materialize-textarea"
					v-model="value"
					v-bind:style="{minHeight: `${1.1 * schema.rows + 2.2}em`, maxHeight: `${1.1 * schema.rows + 2.2}em`}"
					:id="getFieldID(schema)"
					:disabled="disabled"
					:maxlength="schema.max"
					:minlength="schema.min"
					:placeholder="schema.placeholder"
					:readonly="schema.readonly"
					:name="schema.inputName">
			</textarea>
		</div>
		<p v-else>{{value}}</p>
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
			this.characterCount = this.$refs.textarea.value.length;
		},

		methods: {
			countCharacters() {
				let textArea = this.$refs.textarea;
				if(!textArea) {
					this.characterCount = 0;
				} else {
					this.characterCount = this.$refs.textarea.value.length;
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