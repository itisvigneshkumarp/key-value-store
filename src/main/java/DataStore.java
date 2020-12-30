import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
  private String filePath = "datastore/data.txt";
  private ConcurrentHashMap<String, KeyValueStructure> dataStore = new ConcurrentHashMap<>();

  /** Default Constructor */
  public DataStore() {
    File directory = new File("datastore");
    File file = new File(this.filePath);
    if (!directory.exists()) // noinspection ResultOfMethodCallIgnored
    directory.mkdirs();
    if (!file.exists()) {
      try {
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Parameterized Constructor
   *
   * @param filePath - String
   */
  public DataStore(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Getter
   *
   * @return Filepath - String
   */
  public String getFilePath() {
    return filePath;
  }

  /** Helper method for reading the contents of the file */
  private void readFile() {
    try {
      FileInputStream fileInput = new FileInputStream(this.filePath);
      ObjectInputStream objectInput = new ObjectInputStream(fileInput);
      //noinspection unchecked
      this.dataStore = (ConcurrentHashMap<String, KeyValueStructure>) objectInput.readObject();
      objectInput.close();
      fileInput.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /** Helper method to write contents to the file */
  private void writeFile() {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(this.filePath);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      FileChannel fileChannel = fileOutputStream.getChannel();
      fileChannel.lock();
      objectOutputStream.writeObject(this.dataStore);
      objectOutputStream.close();
      fileOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create method for the key-value store without time to live Default time to live is set to -1
   * and will not be checked for expiry
   *
   * @param key - String
   * @param value - Object
   * @return - String
   */
  public String create(String key, Object value) {
    KeyValueStructure valueStructure = new KeyValueStructure(value, 0);
    if (isValid(key, value)) this.dataStore.put(key, valueStructure);
    this.writeFile();
    return "Added";
  }

  /**
   * Create method for the key-value store with time to live Key will be checked for expiry as per
   * the time to live provided
   *
   * @param key - String
   * @param value - Object
   * @param timeToLive - Long
   * @return - String
   */
  public String create(String key, Object value, long timeToLive) {
    long currentInSeconds = new Date().getTime() / 1000;
    long newTimeToLive = currentInSeconds + timeToLive;
    KeyValueStructure valueStructure = new KeyValueStructure(value, newTimeToLive);
    if (isValid(key, value)) this.dataStore.put(key, valueStructure);
    this.writeFile();
    return "Added";
  }

  /**
   * Read method for key-value store to get the value of the specified key.
   *
   * @param key - String
   * @return - String
   */
  public Object read(String key) {
    this.readFile();
    if (validateKey(key)) return getJSONObject(key, this.dataStore.get(key).getValue());
    else return null;
  }

  /**
   * Delete method for key-value store to delete the key specified
   *
   * @param key - String
   * @return - String
   */
  public String delete(String key) {
    this.readFile();
    if (validateKey(key)) {
      this.dataStore.remove(key);
      this.writeFile();
      return "Deleted";
    } else {
      return "Unable to delete";
    }
  }

  /**
   * Method for constraints validation Key constraints - 32 char, unique Value constraints - 16KB
   * File constraints - 1GB
   *
   * @param key - String
   * @param value - Object
   * @return - boolean
   */
  private boolean isValid(String key, Object value) {
    File file = new File(this.filePath);
    if (!file.exists() || !file.isFile())
      throw new IllegalArgumentException("Error: File not found!");
    if (this.dataStore.containsKey(key))
      throw new IllegalArgumentException("Error: Key already exists");
    if (key.length() > 32)
      throw new IllegalArgumentException("Error: Key name size exceeds 32 characters!");
    if (value.toString().getBytes().length > 16384)
      throw new IllegalArgumentException("Error: Value size exceed 16KB!");
    if (file.length() > 1073741824)
      throw new IllegalArgumentException("Error: File size exceeded 1GB!");
    if (!isJSONValid(value)) throw new IllegalArgumentException("Error: Invalid JSON Value");
    return true;
  }

  /**
   * Helper method to return data as JSON object to user
   *
   * @param key - String
   * @param value - Object
   * @return - JSON Object
   */
  private JSONObject getJSONObject(String key, Object value) {
    HashMap<String, Object> data = new HashMap<>();
    data.put(key, value);
    return new JSONObject(data);
  }

  /**
   * Helper method to validate the JSON value
   *
   * @param value - Object
   * @return - boolean
   */
  private boolean isJSONValid(Object value) {
    try {
      new JSONObject(value);
    } catch (JSONException ex) {
      try {
        new JSONArray(value);
      } catch (JSONException ex1) {
        return false;
      }
    }
    return true;
  }

  /**
   * Helper method to validate key expiry and existence
   *
   * @param key - String
   * @return - boolean
   */
  private boolean validateKey(String key) {
    this.readFile();
    long currentInSeconds = new Date().getTime() / 1000;
    if (!this.dataStore.containsKey(key))
      throw new IllegalArgumentException("Error: Key does not exist");
    if (this.dataStore.containsKey(key)
        && this.dataStore.get(key).getTimeToLive() != 0
        && currentInSeconds > this.dataStore.get(key).getTimeToLive())
      throw new IllegalArgumentException("Error: Key has expired!");
    return true;
  }
}
