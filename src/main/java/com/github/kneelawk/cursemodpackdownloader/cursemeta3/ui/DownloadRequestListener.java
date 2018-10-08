package com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui;

import java.nio.file.Path;

public interface DownloadRequestListener {
	public void downloadModpack(Path modpackZip, Path toDir);
}
