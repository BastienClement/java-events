java-events
===========

Observer pattern done right!

```java
import galedric.events.*

//
// The event
//
class MyEvent extends Event {
  private String value;
  
  public MyEvent(String val) {
    this.value = val;
  }
  
  public String getValue() {
    return value;
  }
}

//
// The listener
//
class MyListener extends EventListener {
  public void on(MyEvent event) {
    System.out.println(event.getValue()); 
  }
}

//
// The emitter
//
class MyEmitter extends EventEmitter {
  // Someone will call me maybe!
  private void do_emit() {
    this.emit(new MyEvent("Hello world!"));
  }
  
  // Main
  public static void main(String[] args) {
    MyEmitter emitter = new MyEmitter();
    MyListener listener = new MyListener();
    
    emitter.addListener(listener);
    emitter.do_emit();
    
    // Enjoy !
  }
}
```
