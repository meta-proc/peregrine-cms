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