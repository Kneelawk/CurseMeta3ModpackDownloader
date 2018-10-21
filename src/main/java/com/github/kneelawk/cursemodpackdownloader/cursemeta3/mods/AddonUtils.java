package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileDataJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileId;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.BadResponseCodeException;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddonUtils {
	public static final String GET_ADDON_FILE_FORMAT =
			"https://staging_cursemeta.dries007.net/api/v3/direct/addon/%d/file/%d";
	public static final String GET_ADDON_FILES_FORMAT =
			"https://staging_cursemeta.dries007.net/api/v3/direct/addon/%d/files";

	public static FileDataJson getAddonFile(CloseableHttpClient client,
			Gson gson, FileId id) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(String.format(GET_ADDON_FILE_FORMAT,
				id.getProjectID(), id.getFileID()));

		try (CloseableHttpResponse response = client.execute(get)) {

			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() / 100 == 2) {
				return gson.fromJson(EntityUtils.toString(response.getEntity()),
						FileDataJson.class);
			} else {
				throw new BadResponseCodeException(
						"Bad response status: " + status.toString());
			}
		}
	}

	public static FileDataJson getAddonFileOrLatest(CloseableHttpClient client,
			Gson gson, String minecraftVersion, FileId id)
			throws ClientProtocolException, IOException, ParseException {
		HttpGet request = new HttpGet(
				String.format(GET_ADDON_FILES_FORMAT, id.getProjectID()));

		try (CloseableHttpResponse response = client.execute(request)) {
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() / 100 != 2) {
				throw new BadResponseCodeException(
						"Bad response status: " + status.toString());
			}

			TypeToken<List<FileDataJson>> filesType =
					new TypeToken<List<FileDataJson>>() {
					};
			List<FileDataJson> datas =
					gson.fromJson(EntityUtils.toString(response.getEntity()),
							filesType.getType());

			Map<Long, FileDataJson> fileIds = Maps.newHashMap();
			for (FileDataJson file : datas) {
				fileIds.put(file.getId(), file);
			}

			if (fileIds.containsKey(Long.valueOf(id.getFileID()))) {
				return fileIds.get(Long.valueOf(id.getFileID()));
			} else {
				FastDateFormat format = FastDateFormat.getInstance(
						"yyyy-MM-dd'T'HH:mm:ss", TimeZone.getTimeZone("UTC"));
				FileDataJson newest = null;
				Date newestDate = null;
				for (FileDataJson data : datas) {
					Date fileDate = format.parse(data.getFileDate());
					if (data.getGameVersion().contains(minecraftVersion)
							&& (newest == null
									|| fileDate.compareTo(newestDate) > 0)) {
						newest = data;
						newestDate = fileDate;
					}
				}
				return newest;
			}
		}
	}
}
