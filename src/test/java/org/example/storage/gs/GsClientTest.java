package org.example.storage.gs;

import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GsClientTest {
    //create GsClient with local in-memory Storage
    private GsClient client = new GsClient(LocalStorageHelper.getOptions().getService());

    @Test
    public void testUploadObject_whenObjectIsUploaded_shouldUploadItAndReturnTheNameOfFile() {
        var result = client.uploadObject("bucket-1", "file1.txt", "Test 123".getBytes());
        assertThat(result).isEqualTo("file1.txt");

        var objects = client.listObjects("bucket-1");

        assertThat(objects).isNotEmpty();
        assertThat(objects).containsExactlyInAnyOrderElementsOf(List.of("file1.txt"));
    }

    @Test
    public void testListObject_whenNoObjectInBucket_shouldReturnEmptyList() {
        var objects = client.listObjects("bucket-1");
        assertThat(objects).isEmpty();
    }

    @Test
    public void testListObject_whenTwoObjectsInBucket_shouldReturnListWithItsName() {
        client.uploadObject("bucket-1", "file1.txt", "Test 123".getBytes());
        client.uploadObject("bucket-1", "file2.txt", "Test 456".getBytes());

        var objects = client.listObjects("bucket-1");

        assertThat(objects).hasSize(2);
        assertThat(objects).containsExactlyInAnyOrderElementsOf(List.of("file1.txt", "file2.txt"));
    }

    @Test
    public void testDownloadObject_whenObjectDoesNotExist_shouldReturnEmpty() {
        var result = client.downloadObject("bucket-1", "file1.txt");
        assertThat(result).isEmpty();
    }

    @Test
    public void testDownloadObject_whenObjectExists_shouldReturnArrayOfBytes() {
        //prepare case
        var content = "Test 123";
        client.uploadObject("bucket-1", "file1.txt", content.getBytes());
        //perform tested method
        var result = client.downloadObject("bucket-1", "file1.txt");
        //validate result
        assertThat(result).isNotEmpty();
        assertThat(result).hasValue(content.getBytes());
        //System.out.println(new String(result.get(), StandardCharsets.UTF_8));
    }
}
