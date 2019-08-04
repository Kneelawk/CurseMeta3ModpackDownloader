package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileDataJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileId;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.BadResponseCodeException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AddonUtils {
    public static final String GET_ADDON_FILE_FORMAT =
            "https://cursemeta.dries007.net/%d/%d.json";
    public static final String GET_ADDON_FILES_FORMAT =
            "https://cursemeta.dries007.net/%d/files.json";

    public static FileDataJson getAddonFile(CloseableHttpClient client, Gson gson, FileId id) throws IOException {
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

    public static FileDataJson getAddonFileOrLatest(CloseableHttpClient client, Gson gson, String minecraftVersion,
                                                    FileId id) throws IOException, ParseException {
        try {
            return getAddonFile(client, gson, id);
        } catch (BadResponseCodeException e) {
            return getLatestAddonFile(client, gson, minecraftVersion, id.getProjectID());
        }
    }

    public static FileDataJson getLatestAddonFile(CloseableHttpClient client, Gson gson, String minecraftVersion,
                                                  long projectID) throws IOException, ParseException {
        HttpGet request = new HttpGet(
                String.format(GET_ADDON_FILES_FORMAT, projectID));

        try (CloseableHttpResponse response = client.execute(request)) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() / 100 != 2) {
                throw new BadResponseCodeException(
                        "Bad response status: " + status.toString());
            }

            TypeToken<List<FileDataJson>> filesType =
                    new TypeToken<>() {
                    };
            List<FileDataJson> datas =
                    gson.fromJson(EntityUtils.toString(response.getEntity()),
                            filesType.getType());

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
