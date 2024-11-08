import {Inject, Injectable, Injector} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';
import {webSocket} from 'rxjs/webSocket';
import {AuthenticationService} from './auth.service';
import {
  Action,
  Channel
} from '../enum/live-notification.enum';

@Injectable({
  providedIn: 'root'
})
export class LiveNotificationService {
  public wsSubjects: Subject<any> = new Subject<any>();
  public healthCheck = new BehaviorSubject<boolean>(false);
  public subscribedChannelList: Channel[] = [];
  public subscribedChannelChange = new Subject<any>();
  public refreshTimeSec!: number;
  public data: string[] = [];

  constructor(
    @Inject(Injector) private readonly injector: Injector
  ) {
  }

  private get authService() {
    return this.injector.get(AuthenticationService);
  }

  public startLiveNotification() {
    const user = this.authService.getCurrentUser();
    if (user?.token && !this.healthCheck.getValue()) {

      this.wsSubjects = this.createWebSocketConnection(user.token);
      this.wsSubjects.subscribe((data: any) => {
        this.subscribedChannelChange.next(data.channel as Channel);
        console.log(data);
        this.data.push(JSON.stringify(data));
      });
    }
  }

  setLocalStorageSubscribedChannelList(subscribedChannels: Channel[]) {
    localStorage.setItem('subscribedChannelList', JSON.stringify(subscribedChannels));
  }

  private createWebSocketConnection(token: string) {
    return webSocket({
      url: `ws://localhost:8081/websocket/connect?Authorization=${token}`,
      openObserver: {next: (evn: Event) => this.onConnectionOpen(evn)},
      closeObserver: {next: (closeE: CloseEvent) => this.onConnectionClose(closeE)},
      closingObserver: {next: () => this.onConnectionClosing()}
    });
  }

  public stopLiveNotification() {
    this.wsSubjects.unsubscribe();
    this.clearData();
  }

  public subscribeChannel(channel: Channel) {
    this.subscribedChannelList.push(channel);
    this.setLocalStorageSubscribedChannelList(this.subscribedChannelList);
    this.wsSubjects.next({channel, action: 'subscribe'});
  }

  public unSubscribeChannel(channel: Channel) {
    const index = this.subscribedChannelList.findIndex(channels => channels === channel);
    if (index > -1) {
      this.subscribedChannelList.splice(index, 1);
    }
    this.setLocalStorageSubscribedChannelList(this.subscribedChannelList);
    this.wsSubjects.next({channel, action: 'unsubscribe'});
  }

  private startReConnectionTimer() {
    this.refreshTimeSec = 10;
    const refreshReConnectionTimer = setInterval(() => {
      if (--this.refreshTimeSec <= 0) {
        this.startLiveNotification();
        clearInterval(refreshReConnectionTimer);
      }
    }, 1000);
  }

  /**
   * An Observer that watches when open events occur on the underlying web socket.
   * @param evn
   * @private
   */
  private onConnectionOpen(evn: Event) {
    this.healthCheck.next(true);
  }

  public sendRefreshTokenMessage(token: string) {
    this.wsSubjects.next({
        event: Action.REFRESH_CONNECTION,
        payload: token
      }
    );
  }

  /**
   * An Observer than watches when close events occur on the underlying webSocket
   * @param event
   * @private
   */
  private onConnectionClose(event: CloseEvent) {
    this.healthCheck.next(false);
    this.startReConnectionTimer();
  }

  /**
   * An Observer that watches when a close is about to occur due to unsubscription.
   * @private
   */
  private onConnectionClosing() {
    this.healthCheck.next(false);
    this.startReConnectionTimer();
  }

  private clearData() {
    this.data = [];
  }

}
