package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileId;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;

public class ModpackXmlParser {

	public static FileId parseModpackBin(DocumentBuilder builder,
			Path modpackFile) {
		try {
			Document doc = builder.parse(Files.newInputStream(modpackFile));
			NodeList packageList = doc.getElementsByTagName("package");
			if (packageList.getLength() > 0) {
				Element pack = (Element) packageList.item(0);
				NodeList projectList = pack.getElementsByTagName("project");
				if (projectList.getLength() > 0) {
					Element project = (Element) projectList.item(0);
					int projectId =
							Integer.parseInt(project.getAttribute("id"));
					int fileId = Integer.parseInt(project.getAttribute("file"));
					return new FileJson(projectId, fileId);
				}
			}
		} catch (SAXException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
}
