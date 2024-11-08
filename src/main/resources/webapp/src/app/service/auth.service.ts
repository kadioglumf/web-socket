import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Role} from '../enum/role.enum';
import { Observable} from 'rxjs';
import {ILoginRequest, ILoginResponse} from '../modal/login';
import {LiveNotificationService} from './live-notification.service';

const API_URL = 'http://localhost:8081/websocket';

@Injectable({providedIn: 'root'})
export class AuthenticationService {
  userRole: string[] = [];
  isUserLoggedIn = !!this.getCurrentUser();
  checkTokenExpiredInterval!: any;

  constructor(
    private http: HttpClient,
    private liveNotification: LiveNotificationService,
  ) {
  }

  login(request: ILoginRequest): void {
    this.http.post<ILoginResponse>(API_URL + '/auth/login' , {
      email: request.email,
      password: request.password
    }).subscribe(
      response => {
        if (response.roles.length > 1 || (response.roles.length === 1)) {
          this.userRole = response.roles;
          this.setCurrentUser(response.refreshToken, response.token, response.email, response.id);
          this.isUserLoggedIn = this.getCurrentUser();
        }
      }
    );
  }

  refreshToken(token: string): Observable<any> {
    return this.http.post(API_URL + 'auth/refresh', {}, {
      params: {token}
    });
  }


  getCurrentUser(): any {
    return JSON.parse(localStorage.getItem('currentUser') as string);
  }

  private setCurrentUser(refreshToken: string, token: string, email: string, id: number): void {
    localStorage.setItem('currentUser',
      JSON.stringify({
        refreshToken,
        token,
        email,
        id,
        fullName: email.split('@')[0]
      })
    );
  }


  logout(): void {
    clearInterval(this.checkTokenExpiredInterval);
    localStorage.removeItem('currentUser');
    this.isUserLoggedIn = false;
    this.liveNotification.stopLiveNotification();
  }
}
