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
		<p v-if="schema.charCount">{{ characterCount }}</p>
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
		},
		updated() {
			// if( document.activeElement != this.$refs.textarea ){
			// 	$(this.$refs.textarea).trigger('autoresize');
			// }
			console.log(this.$refs.textarea.value.length);
			this.characterCount = this.$refs.textarea.value.length;
			console.log(this.characterCount);
		}
	}
</script>