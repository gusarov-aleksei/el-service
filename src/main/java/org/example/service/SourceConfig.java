package org.example.service;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "storage.source")
interface  SourceConfig {
    String directory();

    String extension();
}
