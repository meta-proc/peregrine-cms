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