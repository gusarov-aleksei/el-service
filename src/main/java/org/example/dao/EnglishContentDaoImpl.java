package org.example.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.jooq.model.tables.records.PodcastRecord;
import org.example.esl.api.EnglishContent;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.example.dao.jooq.model.tables.Podcast.PODCAST;

/**
 * Simple example of jooq usage
 */
public class EnglishContentDaoImpl implements EnglishContentDao {

    final private String userName;
    final private String password;
    final private String url;
    final private ObjectMapper mapper = new ObjectMapper();
    final static String GLOSSARY = "glossary";
    final static String WHAT_ELSE = "whatElse";
    final static String CULTURE = "cultureNotes";

    public EnglishContentDaoImpl(String userName, String password, String url) {
        this.userName = userName;
        this.password = password;
        this.url = url;
    }

    /**
     * Constructor with very default parameters
     */
    public EnglishContentDaoImpl() {
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
                            createBodyValue(content)
                    )
                    .returning(PODCAST.ID)
                    .fetchOne();
            return record.getId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private JSON createBodyValue(EnglishContent content) {
        var body = new HashMap<String, Object>();
        if (content.getGlossary() != null) body.put(GLOSSARY, content.getGlossary());
        if (content.getWhatElse() != null) body.put(WHAT_ELSE, content.getWhatElse());
        if (content.getCultureNotes() != null) body.put(CULTURE, content.getCultureNotes());
        return JSON.json(JSONObject.toJSONString(body));
    }

    @Override
    public Optional<EnglishContent> getById(int id) {
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            var context = DSL.using(conn, SQLDialect.POSTGRES);
            var record = context.select().from(PODCAST)
                    .where(PODCAST.ID.eq((short) id)).fetchOne();
            return convertRecordToContent(record);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<EnglishContent> convertRecordToContent(Record record) throws JsonProcessingException {
        if (record == null) {
            return Optional.empty();
        }
        EnglishContent content = new EnglishContent();
        content.setMetadata(mapper.readValue(record.getValue(PODCAST.METADATA).data(), Map.class));
        var map = mapper.readValue(record.getValue(PODCAST.BODY).data(), Map.class);
        content.setGlossary((Map<String, String>) map.get(GLOSSARY));
        content.setWhatElse((Map<String, String>) map.get(WHAT_ELSE));
        content.setCultureNotes((String) map.get(CULTURE));
        return Optional.of(content);
    }

    @Override
    public Optional<EnglishContent> fetchByFileName(String fileName) {
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            var records = DSL.using(conn, SQLDialect.POSTGRES)
                    .select().from(PODCAST)
                    .where("json_extract_path_text(metadata::json, 'filename') = '" + fileName +"'")
                    .limit(1)
                    .fetch();
            return convertRecordToContent(records.get(0));
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
