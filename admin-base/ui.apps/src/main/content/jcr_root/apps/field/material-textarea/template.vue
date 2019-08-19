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
		<p v-if="showCharCount">{{ charCount }}</p>
		<p v-if="showWordCount">{{ wordCount }}</p>
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
				return !!(this.schema.charCounter) && !this.schema.readonly;
			},
			charCount(){
				return this.value.length;
			},
			showWordCount(){
				return !!(this.schema.wordCounter) && !this.schema.readonly;
			},
			wordCount(){
				return this.value.split(" ").length;
			}
		}
	}
</script>