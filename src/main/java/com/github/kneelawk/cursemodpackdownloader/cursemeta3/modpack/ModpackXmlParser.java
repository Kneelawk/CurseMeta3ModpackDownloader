package com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModpackXmlParser {
	private static DocumentBuilderFactory factory =
			DocumentBuilderFactory.newInstance();

	public static FileId parseModpackBin(Path modpackFile) {
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
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
					return new FileId(projectId, fileId);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
}
