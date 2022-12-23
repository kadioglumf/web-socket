package com.kadioglumf.socket;


import com.kadioglumf.socket.annotations.Action;
import com.kadioglumf.socket.annotations.ChannelHandler;

/**
 * Incoming message received via WebSocket. The raw message is a JSON
 * string in the following format:
 * <pre>
 * {
 *   "channel": required|String
 *   "action": required|String
 *   "payload": required|String
 * }
 * </pre>
 */
public class IncomingMessage {

  /**
   * Specify the channel for this message. {@link WebSocketRequestDispatcher}
   * will route the request to the corresponding {@link ChannelHandler}.
   */
  private String channel;

  /**
   * Specify the action to take. {@link WebSocketRequestDispatcher} will find
   * the corresponding action method by checking the {@link Action} settings
   */
  private String action;

  /**
   * The payload of the message that an action method will receive as its input.
   */
  private Object payload;

  /**
   * for what purpose the message was sent
   */
  private WsEventType event;

  /**
   * The category type of message
   */
  private WsCategoryType category;


  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  public WsEventType getEvent() {
    return event;
  }

  public void setEvent(WsEventType event) {
    this.event = event;
  }

  public WsCategoryType getCategory() {
    return category;
  }

  public void setCategory(WsCategoryType category) {
    this.category = category;
  }
}
