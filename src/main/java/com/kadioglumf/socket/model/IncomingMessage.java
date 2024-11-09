package com.kadioglumf.socket.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.socket.model.enums.*;
import com.kadioglumf.socket.WebSocketRequestDispatcher;
import com.kadioglumf.socket.annotations.Action;
import com.kadioglumf.socket.converter.WsCategoryTypeConverter;
import com.kadioglumf.socket.converter.WsInfoTypeConverter;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class IncomingMessage {

  /**
   * Specify the channel for this message. {@link WebSocketRequestDispatcher}
   * will route the request to the corresponding channel.
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
}