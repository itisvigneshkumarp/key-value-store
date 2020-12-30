import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DataStoreTest {
  @Test
  void testDefaultFileCreation() {
    Assertions.assertTrue(new File("datastore/data.txt").exists());
  }

  @Test
  void testUserFileCreation() {
    String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\userDataStore.txt";
    DataStore dataStore = new DataStore(filePath);
    Assertions.assertEquals(filePath, dataStore.getFilePath());
  }

  @Test
  void testCreateWithoutTTL() {
    DataStore dataStore = new DataStore();
    Assertions.assertEquals("Added", dataStore.create("name", "vignesh"));
  }

  @Test
  void testCreateWithTTL() {
    DataStore dataStore = new DataStore();
    Assertions.assertEquals("Added", dataStore.create("age", 21, 60));
  }

  @Test
  void testRead() {
    DataStore dataStore = new DataStore();
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          dataStore.read("isExist");
        });
  }

  @Test
  void testReadTTL() throws InterruptedException {
    DataStore dataStore = new DataStore();
    dataStore.create("age", 21, 1);
    TimeUnit.SECONDS.sleep(5);
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          dataStore.read("age");
        });
  }

  @Test
  void testDelete() {
    DataStore dataStore = new DataStore();
    dataStore.create("club", "fcb");
    Assertions.assertEquals("Deleted", dataStore.delete("club"));
  }

  @Test
  void testIsValid() {
    DataStore dataStore = new DataStore();
    String key = "sasdsadsdadasadasdasdasdsadsadasdadasddadadadadasdasdasdadads";
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          dataStore.create(key, "check");
        });
  }

  // replace file paths with right ones while testing
  @Test
  void testFileSize() {
    String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\fileSize.txt";
    DataStore dataStore = new DataStore(filePath);
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          dataStore.create("new", "data");
        });
  }

  @Test
  void testValueSize() throws IOException {
    String data = dataInString();
    DataStore dataStore = new DataStore();
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          dataStore.create("value", data);
        });
  }


  // replace file paths with right ones while testing
  String dataInString() throws IOException {
    FileInputStream fis =
        new FileInputStream(
            System.getProperty("user.dir") + "\\src\\test\\resources\\valueSize.txt");
    byte[] buffer = new byte[10];
    StringBuilder sb = new StringBuilder();
    while (fis.read(buffer) != -1) {
      sb.append(new String(buffer));
      buffer = new byte[10];
    }
    fis.close();
    return sb.toString();
  }

  @Test
  void testThreadSafety() {
    Thread t1 =
        new Thread(
            () -> {
              DataStore d1 = new DataStore();
              d1.create("test", "data");
            });
    Thread t2 =
        new Thread(
            () -> {
              DataStore d2 = new DataStore();
              Assertions.assertThrows(
                  IllegalArgumentException.class,
                  () -> {
                    d2.read("checkdata");
                  });
            });
    t1.start();
    t2.start();
  }
}
