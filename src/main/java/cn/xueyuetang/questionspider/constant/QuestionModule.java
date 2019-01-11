package cn.xueyuetang.questionspider.constant;

public enum QuestionModule {
	ToeflReading(1), IeltsReading(2), ToeflWriting(3),IeltsWriting(4),TofelListening(5),
	IetlsListening(6),TofelSpoking(7),IetlsSpoking(8),SatReading(1),SatMath(2),SatGrammar(3),
	SatWriting(4),SatWord(5),GreBlank(3),GreMath(2),GreReading(1),GreWriting(4),GreWord(5),
	GmatMath(1),GmatSc(2),GmatCr(3),GmatRc(4),GmatIr(5),GmatWriting(6),GmatWord(7);
	private final Integer typeId;

	private QuestionModule(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getTypeId() {
		return typeId;
	}

}
