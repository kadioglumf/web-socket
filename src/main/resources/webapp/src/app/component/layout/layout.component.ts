import {AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {LiveNotificationService} from '../../service/live-notification.service';
import {Channel} from '../../enum/live-notification.enum';
import {AuthenticationService} from "../../service/auth.service";

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit, AfterViewInit {
  channel = Channel;


  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    public liveNotification: LiveNotificationService,
    public authenticationService: AuthenticationService,
  ) {
    this.liveNotification.startLiveNotification();
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.changeDetectorRef.detectChanges();
  }


  toggleChannel(channel: Channel) {
    if (this.checkChannel(channel)) {
      this.liveNotification.unSubscribeChannel(channel);
    } else {
      this.liveNotification.subscribeChannel(channel);
    }
  }

  checkChannel(channel: Channel): boolean {
    return this.liveNotification.subscribedChannelList.includes(channel);
  }
}
