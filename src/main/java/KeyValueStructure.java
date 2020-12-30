import java.io.Serializable;

public class KeyValueStructure implements Serializable {
  Object value;
  long timeToLive;

  public KeyValueStructure() {}

  public KeyValueStructure(Object value) {
    this.value = value;
    this.timeToLive =
        0; // default time to live for the key will be set as 0 and it will not be checked.
  }

  public KeyValueStructure(Object value, long timeToLive) {
    this.value = value;
    this.timeToLive = timeToLive;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public long getTimeToLive() {
    return timeToLive;
  }

  public void setTimeToLive(long timeToLive) {
    this.timeToLive = timeToLive;
  }
}
