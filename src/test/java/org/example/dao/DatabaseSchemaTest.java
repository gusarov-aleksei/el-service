package org.example.dao;

import org.jooq.Field;
import org.jooq.Named;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test validates Database schema created with Flyway
 */
public class DatabaseSchemaTest extends AbstractDatabaseTest {

    @Test
    void testTableNames_shouldContainsTablesDefinedNames() {
        try (Connection conn = DriverManager.getConnection(dbContainer.getJdbcUrl(), USER_NAME, USER_PASSWORD)) {
            var schema = retrieveSchema(conn , "el_service");

            assertThat(schema).isPresent();
            assertThat(schema.get().getTables())
                    .isNotNull().hasSize(2)
                    .extracting(Named::getName)
                    .containsExactlyInAnyOrder("podcast", "flyway_schema_history");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Schema> retrieveSchema(final Connection conn, final String schemaName) {
        var metadata = DSL.using(conn, SQLDialect.POSTGRES).meta();
        return  metadata.getSchemas().stream().filter(sc -> schemaName.equals(sc.getName())).findFirst();
    }

    @Test
    void testPodcastTableFields_shouldContainsFieldsWithDefinedType() {
        try (Connection conn = DriverManager.getConnection(dbContainer.getJdbcUrl(), USER_NAME, USER_PASSWORD)) {
            var schema = retrieveSchema(conn , "el_service");

            assertThat(schema).isPresent();

            var podcastTable = schema.get().getTables().stream().filter(t -> "podcast".equals(t.getName())).findFirst();

            assertThat(podcastTable).isPresent();

            assertThat(podcastTable.get().fields())
                    .extracting(Field::getName)
                    .containsExactlyInAnyOrder("id", "metadata", "body");
            
            var table = podcastTable.get();
            var idField = retrieveFieldByName(table, "id");
            assertThat(idField).isPresent();
            assertThat(idField.get().getDataType().nullable()).isFalse();

            var metadataField = retrieveFieldByName(table, "metadata");
            assertThat(metadataField).isPresent();
            assertThat(metadataField.get().getType().getName()).isEqualTo("org.jooq.JSONB");
            assertThat(metadataField.get().getDataType().nullable()).isTrue();


            var bodyField = retrieveFieldByName(table, "body");
            assertThat(bodyField).isPresent();
            assertThat(bodyField.get().getType().getName()).isEqualTo("org.jooq.JSON");
            assertThat(bodyField.get().getDataType().nullable()).isTrue();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Field<?>> retrieveFieldByName(Table<?> table, String name) {
        return Arrays.stream(table.fields())
                .filter(f -> name.equals(f.getName())).findFirst();
    }
}
