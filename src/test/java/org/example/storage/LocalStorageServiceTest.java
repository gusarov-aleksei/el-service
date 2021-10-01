package org.example.storage;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.example.storage.local.LocalStorageService;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestProfile(LocalStorageServiceTestProfile.class)
public class LocalStorageServiceTest {

    @Inject
    StorageService storageService;

    /**
     * Tests instance class returned according to configuration
     */
    @Test
    public void testStorageServiceConfig_whenStorageTypeLocal_shouldBeLocalStorageService() {
        assertThat(storageService).isNotNull();
        assertThat(storageService).isExactlyInstanceOf(LocalStorageService.class);
    }

    @Test
    public void testDeleteFiles_whenFileDoesNotExist_shouldReturnNotOK() {
        var name = UUID.randomUUID() + ".txt";
        var result = storageService.deleteFiles(new String[]{name});
        assertThat(result)
                .isNotNull().hasSize(1).extracting("key").contains(name);
        // check that value contains message with 'NoSuchFileException' and name
        assertThat(result).extracting("value", String.class)
                .filteredOn(value -> value.contains("NoSuchFileException"))
                .filteredOn(value -> value.contains(name)).hasSize(1);
    }

    @Test
    public void testLocalStorageServiceOperations_Scenario1() throws IOException {
        //validate several operation. steps are below
        //write file
        //list files and check if newly created file name exist
        //read file and check if content the same
        //remove file and check result
        //list file and check if created file at step 1 is removed

        //given test data
        var randomName = UUID.randomUUID() + ".txt";
        var body = "Test message".getBytes();

        storageService.writeBytesToFile(randomName, body);

        var list = storageService.listFileNames("", ".txt");
        assertThat(list).contains(randomName);

        var actualBody = storageService.readBytesFormFile(randomName);
        assertThat(actualBody).isEqualTo(body);

        var deleteResult = storageService.deleteFiles(new String[]{randomName});
        assertThat(deleteResult)
                .isNotNull()
                .hasSize(1)
                .containsExactly(Map.entry(randomName, "OK"));

        list = storageService.listFileNames("", ".txt");
        //verify there is no file name which was deleted
        assertThat(list).doesNotContain(randomName);
    }

    @Test
    public void testLocalStorageServiceOperations_Scenario2() throws IOException {
        // create prerequisites
        // existing file name and body
        var randomName = UUID.randomUUID() + ".txt";
        var body = "Test message".getBytes();
        // write it to storage
        storageService.writeBytesToFile(randomName, body);
        // non-existing file name
        var nonExistingFile = "non-existing-file.txt";

        //call file deletion
        var deleteResult = storageService.deleteFiles(
                new String[]{randomName, nonExistingFile});
        //and then validate result
        //expected two records in result containing file name and operation result:
        //one record with successful delete, another one with unfortunate attempt of non-existing file deleting
        assertThat(deleteResult)
                .isNotNull()
                .hasSize(2)
                .contains(Map.entry(randomName, "OK"));
        //get record with non-existing-file.txt and check NoSuchFileException
        assertThat(deleteResult)
                .filteredOn(entry -> nonExistingFile.equals(entry.getKey()))
                .hasSize(1)
                .anyMatch(entry -> entry.getValue()!=null && entry.getValue().contains("NoSuchFileException"));
    }
}
