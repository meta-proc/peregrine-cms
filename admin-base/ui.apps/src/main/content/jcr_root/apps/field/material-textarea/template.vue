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
		<admin-components-charactercounter v-if="showCharCount" :text="value"></admin-components-charactercounter>
		<admin-components-wordcounter v-if="showWordCount" :text="value"></admin-components-wordcounter>
	</div>
</template>

<script>
	export default {
		mixins: [ VueFormGenerator.abstractField ],

		data() {
			return {
			}
		},
		computed: {
			showCharCount(){
				return !this.schema.readonly && this.schema.charCounter === 'true';
			},
			showWordCount(){
				return !this.schema.readonly && this.schema.wordCounter === 'true';
			}
		}
	}
</script>

<style scoped>
  .materialize-textarea {
    min-height: 3.5rem;
  }
</style>
