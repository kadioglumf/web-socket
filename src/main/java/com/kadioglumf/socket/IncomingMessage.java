package com.kadioglumf.socket;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.socket.annotations.Action;
import com.kadioglumf.socket.annotations.ChannelHandler;

import javax.persistence.Convert;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomingMessage {

  /**
   * Specify the channel for this message. {@link WebSocketRequestDispatcher}
   * will route the request to the corresponding {@link ChannelHandler}.
   */
  private String channel;

  /**
   * Specify the action to take. {@link WebSocketRequestDispatcher} will find
   * the corresponding action method by checking the {@link Action} settings
   * action must be in {@link ActionType}
   */
  private String action;

  /**
   * The payload of the message that an action method will receive as its input.
   */
  private Object payload;

  /**
   * for what purpose the message was sent
   */
  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  /**
   * The category type of message
   */
  @Convert(converter = WsCategoryTypeConverter.class)
  private WsCategoryType category;

  private WsSendingType sendingType;
  private RoleType role;
  private Long userId;


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

  public WsInfoType getInfoType() {
    return infoType;
  }

  public void setInfoType(WsInfoType infoType) {
    this.infoType = infoType;
  }

  public WsCategoryType getCategory() {
    return category;
  }

  public void setCategory(WsCategoryType category) {
    this.category = category;
  }

  public WsSendingType getSendingType() {
    return sendingType;
  }

  public void setSendingType(WsSendingType sendingType) {
    this.sendingType = sendingType;
  }

  public RoleType getRole() {
    return role;
  }

  public void setRole(RoleType role) {
    this.role = role;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}