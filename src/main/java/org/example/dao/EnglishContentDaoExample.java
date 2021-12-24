package org.example.dao;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.jooq.model.tables.records.PodcastRecord;
import org.example.esl.EnglishContent;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import static org.example.dao.jooq.model.tables.Podcast.PODCAST;

/**
 * Simple example of jooq usage
 */
public class EnglishContentDaoExample implements EnglishContentDao {

    final private String userName;
    final private String password;
    final private String url;

    public EnglishContentDaoExample(String userName, String password, String url) {
        this.userName = userName;
        this.password = password;
        this.url = url;
    }

    /**
     * Constructor with very default parameters
     */
    public EnglishContentDaoExample() {
        this.userName = "postgres";
        this.password = "postgres";
        this.url = "jdbc:postgresql:postgres";
    }

    @Override
    public int create(EnglishContent content) {
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            var context = DSL.using(conn, SQLDialect.POSTGRES);
            PodcastRecord record = context.insertInto(PODCAST, PODCAST.METADATA, PODCAST.BODY)
                    .values(
                            JSONB.jsonb(JSONObject.toJSONString(content.getMetadata())),
                            JSON.json(JSONObject.toJSONString(content.getGlossary()))
                    )
                    .returning(PODCAST.ID)
                    .fetchOne();
            return record.getId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<EnglishContent> getById(int id) {
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            var context = DSL.using(conn, SQLDialect.POSTGRES);
            var record = context.select().from(PODCAST)
                    .where(PODCAST.ID.eq((short) id)).fetchOne();
            if (record == null) {
                return Optional.empty();
            } else {
                EnglishContent content = new EnglishContent();
                var mapper = new ObjectMapper();
                content.setMetadata(
                        mapper.readValue(record.getValue(PODCAST.METADATA).data(), Map.class));
                content.setGlossary(
                        mapper.readValue(record.getValue(PODCAST.BODY).data(), Map.class));
                return Optional.of(content);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
