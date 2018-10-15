package com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddonUtils {
	public static final String GET_ADDON_FILE_FORMAT =
			"https://staging_cursemeta.dries007.net/api/v3/direct/addon/%d/file/%d";
	public static final String GET_ADDON_FILES_FORMAT =
			"https://staging_cursemeta.dries007.net/api/v3/direct/addon/%d/files";

	public static FileDataJson getAddonFile(CloseableHttpClient client,
			Gson gson, int projectId, int fileId)
			throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(
				String.format(GET_ADDON_FILE_FORMAT, projectId, fileId));

		try (CloseableHttpResponse response = client.execute(get)) {
			return gson.fromJson(EntityUtils.toString(response.getEntity()),
					FileDataJson.class);
		}
	}

	public static FileDataJson getAddonFileOrLatest(CloseableHttpClient client,
			Gson gson, String minecraftVersion, int projectId, int fileId)
			throws ClientProtocolException, IOException, ParseException {
		HttpGet request =
				new HttpGet(String.format(GET_ADDON_FILES_FORMAT, projectId));

		try (CloseableHttpResponse response = client.execute(request)) {
			TypeToken<List<FileDataJson>> filesType =
					new TypeToken<List<FileDataJson>>() {
					};
			List<FileDataJson> files =
					gson.fromJson(EntityUtils.toString(response.getEntity()),
							filesType.getType());

			Map<Long, FileDataJson> fileIds = Maps.newHashMap();
			for (FileDataJson file : files) {
				fileIds.put(file.getId(), file);
			}

			if (fileIds.containsKey(Long.valueOf(fileId))) {
				return fileIds.get(Long.valueOf(fileId));
			} else {
				FastDateFormat format = FastDateFormat.getInstance(
						"yyyy-MM-dd'T'HH:mm:ss", TimeZone.getTimeZone("UTC"));
				FileDataJson newest = null;
				Date newestDate = null;
				for (FileDataJson file : files) {
					Date fileDate = format.parse(file.getFileDate());
					if (file.getGameVersion().contains(minecraftVersion)
							&& (newest == null
									|| fileDate.compareTo(newestDate) > 0)) {
						newest = file;
						newestDate = fileDate;
					}
				}
				return newest;
			}
		}
	}
}
