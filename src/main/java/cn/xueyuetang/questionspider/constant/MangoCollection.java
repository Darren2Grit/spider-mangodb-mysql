package cn.xueyuetang.questionspider.constant;

public enum MangoCollection {
	Reading("reading"), Writing("writing"), Spoking("Spoking"), Listening("Listenging"),Sat("SAT"),GRE("GRE"),GMAT("GMAT");

	private final String name;

	private MangoCollection(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
