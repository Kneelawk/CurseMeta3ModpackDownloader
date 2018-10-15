module com.github.kneelawk.cursemodpackdownloader.cursemeta3 {
	exports com.github.kneelawk.cursemodpackdownloader.cursemeta3;

	requires java.xml;
	requires java.sql;

	requires transitive javafx.controls;
	requires javafx.graphics;

	requires com.google.common;
	requires gson;
	requires org.apache.httpcomponents.httpclient;
	requires org.apache.httpcomponents.httpcore;
	requires commons.codec;
	requires org.apache.commons.lang3;
}