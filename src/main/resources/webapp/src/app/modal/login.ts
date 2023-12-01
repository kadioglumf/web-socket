import {FormControl} from '@angular/forms';

export interface Login {
  email: FormControl<string>;
  password: FormControl<string>;
}

export interface ILoginRequest {
  email: string;
  password: string;
}

export interface ILoginResponse {
  id: number;
  username: string;
  email: string;
  roles: string[];
  token: string;
  refreshToken: string;
  type: string;
}
