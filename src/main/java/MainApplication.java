import java.util.concurrent.TimeUnit;

public class MainApplication {
  public static void main(String[] args) throws InterruptedException {
    // create a datastore
    DataStore dataStore = new DataStore();

    // create a key-value pair
    //    String result = dataStore.create("name", "vignesh");
    //    System.out.println(result);
    //
    //    // read the key
    //    System.out.println(dataStore.read("name"));
    //
    //    // create a key with time to live
    //    String result2 = dataStore.create("age", 21, 3);
    //    System.out.println(result2);
    //
    //    // read the key after 3 seconds
    //    TimeUnit.SECONDS.sleep(5);
    //    System.out.println(dataStore.read("age"));
    //
    //    // delete a key
    //    System.out.println(dataStore.delete("name"));
    //
    //    // delete an expired key
    //    System.out.println(dataStore.delete("age"));
    //
    //    // insert an exisiting key
    //    System.out.println(dataStore.create("club", "fcb"));
    //    System.out.println(dataStore.create("club", "spurs"));
    //
    //    // test thread safety
    //    Thread t1 =
    //        new Thread(
    //            () -> {
    //              DataStore d1 = new DataStore();
    //              d1.create("test", "data");
    //            });
    //
    //    Thread t2 =
    //        new Thread(
    //            () -> {
    //              DataStore d2 = new DataStore();
    //              d2.create("test2", "data2");
    //            });
    //    t1.start();
    //    t2.start();
    //
    //    // user given file path
    //    DataStore d2 =
    //        new
    // DataStore("D:\\Code\\home\\custom_datastore\\src\\test\\resources\\userDataStore.txt");
    //    System.out.println(d2.getFilePath());
    //    System.out.println(dataStore.create("dummy", "value"));
    //    System.out.println(dataStore.delete("dummy"));

  }
}
