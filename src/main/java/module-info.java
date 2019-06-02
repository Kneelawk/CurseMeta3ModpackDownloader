module com.github.kneelawk.cursemodpackdownloader.cursemeta3 {
	exports com.github.kneelawk.cursemodpackdownloader.cursemeta3;
	exports com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;
	exports com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

	opens com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json to gson;

	requires java.xml;
	requires java.sql;
	requires java.naming;
	
	requires jdk.zipfs;

	requires transitive javafx.controls;
	requires javafx.graphics;

	requires com.google.common;
	requires gson;
	requires org.apache.httpcomponents.httpclient;
	requires org.apache.httpcomponents.httpcore;
	requires org.apache.commons.codec;
	requires org.apache.commons.lang3;
}